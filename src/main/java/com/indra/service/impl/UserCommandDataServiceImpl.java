package com.indra.service.impl;

import com.indra.db.repository.UserDAO;
import com.indra.db.entity.User;
import com.indra.model.dto.UserDTO;
import com.indra.model.mapper.UserMapper;
import com.indra.service.UserCommandService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class UserCommandDataServiceImpl implements UserCommandService {

    private final UserDAO userDAO;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    final Function<UserMapper, Function<UserDTO, User>> MAP_TO_ENTITY_FUNCTION = mapper -> mapper::map;
    final Function<UserDTO, User> MAP_TO_ENTITY = MAP_TO_ENTITY_FUNCTION.apply(userMapper);
//    final Function<User, User> SAVE_USER = userDAO::save;

    @Override
    public UserDTO addUser(@NonNull UserDTO userDTO) {
        return MAP_TO_ENTITY
                .andThen(userDAO::save)
                .andThen(userMapper::map)
                .apply(userDTO);
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
