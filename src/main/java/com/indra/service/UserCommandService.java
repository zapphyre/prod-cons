package com.indra.service;

import com.indra.model.dto.UserDTO;

import java.util.List;

public interface UserCommandService {
    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    int deleteAllUsers();
}
