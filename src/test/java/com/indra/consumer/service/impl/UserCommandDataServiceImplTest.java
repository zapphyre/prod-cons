package com.indra.consumer.service.impl;

import com.indra.db.repository.UserDAO;
import com.indra.db.entity.User;
import com.indra.model.dto.UserDTO;
import com.indra.service.impl.UserCommandDataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

public class UserCommandDataServiceImplTest {

    @Test
    void testAddUserSuccess() {
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        User saved = User.builder()
                .id(11L)
                .name("somename")
                .build();

        Mockito.when(userDAO.save(Mockito.any())).thenReturn(saved);

        UserCommandDataServiceImpl service = new UserCommandDataServiceImpl(userDAO);
        UserDTO dto = service.addUser(UserDTO.builder().build());

        Assertions.assertTrue(Objects.nonNull(dto.getId()));
    }

    @Test
    void testAddUserFailure() {
        UserDAO userDAO = Mockito.mock(UserDAO.class);

        Mockito.when(userDAO.save(Mockito.any())).thenReturn(null);

        UserCommandDataServiceImpl service = new UserCommandDataServiceImpl(userDAO);
        UserDTO dto = service.addUser(UserDTO.builder().build());

        Assertions.assertTrue(Objects.isNull(dto));
    }

    @Test
    void testFindAllReturnsTheSameLength() {
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        List<User> userList = List.of(User.builder().build(), User.builder().build());

        Mockito.when(userDAO.findAll()).thenReturn(userList);

        UserCommandDataServiceImpl service = new UserCommandDataServiceImpl(userDAO);
        List<UserDTO> dtoList = service.getAllUsers();

        Assertions.assertEquals(userList.size(), dtoList.size());
    }

    @Test
    void testUserDeletionReturnsTheSameAffectedRowsCountLikeDbLayer() {
        final int DELETED_CNT = 21;
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.deleteAll()).thenReturn(DELETED_CNT);

        UserCommandDataServiceImpl service = new UserCommandDataServiceImpl(userDAO);
        int deleteAllUsers = service.deleteAllUsers();

        Assertions.assertEquals(DELETED_CNT, deleteAllUsers);
    }
}
