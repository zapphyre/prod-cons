package com.indra.consumer.db;


import com.indra.consumer.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class UserDAO {

    private final TxLocking txLocking;

    Session getHibernateSession() {
        return AppSessionFactory.getSessionFactory().getCurrentSession();
    }

    protected  <R> R doInTx(Function<Session, R> stmt) {
        Transaction transaction = null;
        R res = null;
        try {
            Session currentSession = getHibernateSession();
            txLocking.waitIfTxInProgress(currentSession);

            transaction = currentSession.beginTransaction();

            res = stmt.apply(currentSession);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            log.error("cannot commit save transaction", e);
        }

        txLocking.notifyTxLock();

        return res;
    }

    public User save(User user) {

        doInTx(session -> session.save(user)); // i could cast the result to Long and assign it to entity as it's id just for a sake to return something, but it modifies the parameter anyway...

        return user;
    }

    public List<User> findAll() {
        List<User> users = new LinkedList<>();

        try {
            Session currentSession = AppSessionFactory.getSessionFactory().getCurrentSession();

            txLocking.waitIfTxInProgress(currentSession);

            Transaction transaction = currentSession.beginTransaction();

            users = currentSession.createQuery("select U from User U", User.class).list();

            transaction.commit();
        } catch (Exception e) {
            log.error("cannot fetch results", e);
        }

        txLocking.notifyTxLock();
        return users;
    }

    public int deleteAll() {
        int affectedCnt = -1;
        try {
            Session currentSession = AppSessionFactory.getSessionFactory().getCurrentSession();
            txLocking.waitIfTxInProgress(currentSession);

            Transaction transaction = currentSession.beginTransaction();

            affectedCnt = currentSession.createQuery("delete from User").executeUpdate();

            transaction.commit();

            return affectedCnt;
        } catch (Exception e) {
            log.error("cannot commit delete transaction", e);
        }

        txLocking.notifyTxLock();
        return affectedCnt;
    }


}