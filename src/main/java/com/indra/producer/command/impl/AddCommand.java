package com.indra.producer.command.impl;

import com.indra.producer.command.Command;
import com.indra.model.dto.UserDTO;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.service.UserCommandService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class AddCommand implements Command {

    @NonNull
    private final UserDTO payload;

    @Override
    public StringServiceActionResult execute(UserCommandService usr) { // really to justify 'I' form SOLID principles, the service should implement 3 different interfaces owned by the producer module (add, query, delete) but I find it sufficient to demonstrate application objective this way now
        UserDTO u = usr.addUser(payload);

        return transformToWritableResult(Objects.nonNull(u.getId()) ? "user added" : "add command failed");
    }
}
