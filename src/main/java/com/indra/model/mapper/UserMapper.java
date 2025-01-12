package com.indra.model.mapper;

import com.indra.model.dto.UserDTO;
import com.indra.db.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDTO map(User user);

    User map(UserDTO dto);

    List<UserDTO> map(List<User> users);
}
