package com.indra.consumer.db;

import com.indra.consumer.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

public class UserDAOTest {

    @Test
    void canSaveReadDeleteEntity() {
        UserDAO userDAO = new UserDAO(AppSessionFactory.getSessionFactory());

        User u = User.builder()
                .name("user1")
                .build();

        Assertions.assertNull(u.getId());

        userDAO.save(u);

        Assertions.assertNotNull(u.getId());

        List<User> all = userDAO.findAll();

        Assertions.assertEquals(1, all.size());

        userDAO.deleteAll();

        all = userDAO.findAll();

        Assertions.assertEquals(0, all.size());
    }

}
