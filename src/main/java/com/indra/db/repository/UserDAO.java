package com.indra.db.repository;


import com.indra.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;

@Slf4j
public class UserDAO extends TransactionalDAO {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User save(User user) {
        return doInTx(s -> s.merge(user));
    }

    public List<User> findAll() {
        return doInTx(s -> s.createQuery("select U from User U", User.class).list());
    }

    public int deleteAll() {
        return doInTx(s -> s.createQuery("delete from User").executeUpdate());
    }
}