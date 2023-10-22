package com.indra.consumer.service.impl;

import com.indra.consumer.db.UserDAO;
import com.indra.model.mapper.UserMapper;
import com.indra.model.dto.UserDTO;
import com.indra.consumer.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

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
    public int deleteAllUsers() {
        return userDAO.deleteAll();
    }
}