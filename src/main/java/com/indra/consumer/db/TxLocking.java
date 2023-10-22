package com.indra.consumer.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
 abstract class TxLocking { // abstract modifier here is not really necessary; however it notifies programmer this class should not be instantiated on it's own

    private final SessionFactory sessionFactory;
    private final Object TX_LOCK = new Object();

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    <R> R doInTx(Function<Session, R> stmt) {
        Transaction transaction = null;
        R res = null;

        try {
            waitIfTxInProgress(getCurrentSession());

            transaction = getCurrentSession().beginTransaction();

            res = stmt.apply(getCurrentSession());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            log.error("cannot commit transaction", e);
        }

        notifyTxLock();

        return res;
    }

    void waitIfTxInProgress(Session session) {
        if (session.getTransaction().isActive())
            waitTxLock();
    }

    void waitTxLock() {
        synchronized (TX_LOCK) {
            try {
                TX_LOCK.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void notifyTxLock() {
        synchronized (TX_LOCK) {
            TX_LOCK.notify();
        }
    }

}
