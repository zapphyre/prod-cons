package com.indra.db;


import com.indra.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UserDAO {

    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;

    public UserDAO() {
        sessionFactory = getSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create registry
                registry = new StandardServiceRegistryBuilder().configure().build();

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public User save(User user) {
        Transaction transaction = null;

        try {
            Session currentSession = sessionFactory.getCurrentSession();
            transaction = currentSession.beginTransaction();

            user.setId((Long) currentSession.save(user));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            log.error("cannot commit transaction", e);
        }

        return user;
    }

    public List<User> findAll() {
        List<User> users = new LinkedList<>();

        try {
            Session currentSession = sessionFactory.getCurrentSession();
            Transaction transaction = currentSession.beginTransaction();

            users = currentSession.createQuery("select U from User U", User.class).list();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}