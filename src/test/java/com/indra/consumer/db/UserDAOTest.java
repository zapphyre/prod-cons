package com.indra.consumer.db;

import com.indra.db.AppSessionFactory;
import com.indra.db.repository.UserDAO;
import com.indra.db.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
