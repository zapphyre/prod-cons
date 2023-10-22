package com.indra.service.impl;

import com.indra.command.Command;
import com.indra.db.UserDAO;
import com.indra.mapper.UserMapper;
import com.indra.model.dto.UserDTO;
import com.indra.model.entity.User;
import com.indra.queue.FIFOQueue;
import com.indra.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserCommandDataServiceImpl implements UserCommandService {

    private final UserDAO userDAO;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public boolean addUser(UserDTO userDTO) {
        return Optional.ofNullable(userDAO.save(userMapper.map(userDTO))).isPresent();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.map(userDAO.findAll());
    }

    @Override
    public boolean deleteAllUsers() {
        return false;
    }
}