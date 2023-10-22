package com.indra.consumer.model.mapper;

import com.indra.consumer.entity.User;
import com.indra.model.dto.UserDTO;
import com.indra.model.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

public class UserMapperTest {

    @Test
    void testEntityToDtoMapping() {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);

        final Long USER_ID = 3124L;
        final UUID USER_UUID = UUID.randomUUID();
        final String USER_NAME = "veryname";

        User user = User.builder()
                .id(USER_ID)
                .uuid(USER_UUID)
                .name(USER_NAME)
                .build();

        UserDTO dto = mapper.map(user);

        Assertions.assertEquals(USER_ID, dto.getId());
        Assertions.assertEquals(USER_UUID, dto.getUuid());
        Assertions.assertEquals(USER_NAME, dto.getName());
    }
}
