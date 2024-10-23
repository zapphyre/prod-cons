package com.indra.db.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
abstract class TransactionalDAO { // abstract modifier here is not really necessary; however it notifies programmer this class should not be instantiated on it's own

    private final SessionFactory sessionFactory;
    private final static Object TX_LOCK = new Object();

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    <R> R doInTx(Function<Session, R> stmt) {
        return Optional.ofNullable(waitIfTxInProgress(getCurrentSession()))
                .map(s -> new TransactionalStmtRunner<>(s, () -> stmt.apply(s)))
                .map(TransactionalStmtRunner::commitTxReturning)
                .orElseThrow();
    }

    Session waitIfTxInProgress(Session session) {
        if (session.getTransaction().isActive())
            waitTxLock();

        return session;
    }

    static void waitTxLock() {
        synchronized (TX_LOCK) {
            try {
                TX_LOCK.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void notifyTxLock() {
        synchronized (TX_LOCK) {
            TX_LOCK.notify();
        }
    }

    record TransactionalStmtRunner<R>(Session session, Supplier<R> stmtRunner) {
        public R commitTxReturning() {
            session.getTransaction().begin();
            R res = stmtRunner.get();
            session.getTransaction().commit();

            notifyTxLock();

            return res;
        }
    }

}
