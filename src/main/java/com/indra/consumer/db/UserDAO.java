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
public class UserDAO extends TxLocking {

    //it's parametrized just for the sake of testing, static fields make things tricky and terrible
    public UserDAO(Session session) {
        super(session);
    }

    public User save(User user) {

        doInTx(s -> s.save(user)); // i could cast the result to Long and assign it to entity as it's id just for the sake of returning something, but it modifies the parameter anyway...

        return user;
    }

    public List<User> findAll() {
        return doInTx(s -> s.createQuery("select U from User U", User.class).list());
    }

    public int deleteAll() {
        return doInTx(s -> s.createQuery("delete from User").executeUpdate());
    }
}