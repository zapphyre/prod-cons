package com.indra.service;

import com.indra.model.dto.UserDTO;
import com.indra.model.pojo.StringServiceActionResult;

import java.util.List;

public interface UserCommandService {
    boolean addUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    boolean deleteAllUsers();
}
