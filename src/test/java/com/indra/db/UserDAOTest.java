package com.indra.db;

import com.indra.model.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserDAOTest {

    @Test
    void canSaveAndReadEntity() {
        UserDAO userDAO = new UserDAO();

        User u = User.builder()
                .name("asfasdf")
                .id(12L)
                .build();

        User u2 = User.builder()
                .name("asfasdf asdfa")
                .id(1122L)
                .build();

        userDAO.save(u);
        userDAO.save(u2);

        List<User> all = userDAO.findAll();
    }
}
