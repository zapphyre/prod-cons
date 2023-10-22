package com.indra.consumer.service;

import com.indra.model.dto.UserDTO;

import java.util.List;

public interface UserCommandService {
    boolean addUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    int deleteAllUsers();
}
