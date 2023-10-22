package com.indra.db;


import com.indra.model.entity.User;
import lombok.RequiredArgsConstructor;
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
    private Session session;

    public UserDAO() {
        this.session = getSessionFactory().getCurrentSession();
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
            transaction = session.beginTransaction();

            user.setId((Long) session.save(user));

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
            users = session.createQuery("select U from User U", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}