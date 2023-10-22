package com.indra.consumer.db;


import com.indra.consumer.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UserDAO {

//    private StandardServiceRegistry registry;
//    private SessionFactory sessionFactory;

    private Object TX_LOCK = new Object();

//    public UserDAO() {
//        sessionFactory = getSessionFactory();
//    }
//
//    public SessionFactory getSessionFactory() {
//        if (sessionFactory == null) {
//            try {
//                // Create registry
//                registry = new StandardServiceRegistryBuilder().configure().build();
//
//                // Create MetadataSources
//                MetadataSources sources = new MetadataSources(registry);
//
//                // Create Metadata
//                Metadata metadata = sources.getMetadataBuilder().build();
//
//                // Create SessionFactory
//                sessionFactory = metadata.getSessionFactoryBuilder().build();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                if (registry != null) {
//                    StandardServiceRegistryBuilder.destroy(registry);
//                }
//            }
//        }
//        return sessionFactory;
//    }
//
//    public void shutdown() {
//        if (registry != null) {
//            StandardServiceRegistryBuilder.destroy(registry);
//        }
//    }

    public User save(User user) {
        Transaction transaction = null;

        try {
            Session currentSession = AppSessionFactory.getSessionFactory().getCurrentSession();

            if (currentSession.getTransaction().isActive()) waitTxLock();

            transaction = currentSession.beginTransaction();

            currentSession.persist(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            log.error("cannot commit save transaction", e);
        }

        notifyTxLock();
        return user;
    }

    public List<User> findAll() {
        List<User> users = new LinkedList<>();

        try {
            Session currentSession = AppSessionFactory.getSessionFactory().getCurrentSession();

            if (currentSession.getTransaction().isActive())
                waitTxLock();

            Transaction transaction = currentSession.beginTransaction();

            users = currentSession.createQuery("select U from User U", User.class).list();

            transaction.commit();
        } catch (Exception e) {
            log.error("cannot fetch results", e);
        }

        notifyTxLock();
        return users;
    }

    public int deleteAll() {
        int affectedCnt = -1;
        try {
            Session currentSession = AppSessionFactory.getSessionFactory().getCurrentSession();
            if (currentSession.getTransaction().isActive()) waitTxLock();

            Transaction transaction = currentSession.beginTransaction();

            affectedCnt = currentSession.createQuery("delete from User").executeUpdate();

            transaction.commit();

            return affectedCnt;
        } catch (Exception e) {
            log.error("cannot commit delete transaction", e);
        }

        notifyTxLock();
        return affectedCnt;
    }

    private void waitTxLock() {
        synchronized (TX_LOCK) {
            try {
                TX_LOCK.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void notifyTxLock() {
        synchronized (TX_LOCK) {
            TX_LOCK.notify();
        }
    }
}