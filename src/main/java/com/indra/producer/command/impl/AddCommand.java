package com.indra.producer.command.impl;

import com.indra.producer.command.Command;
import com.indra.model.dto.UserDTO;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.consumer.service.UserCommandService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddCommand implements Command {

    @NonNull
    private final UserDTO payload;

    @Override
    public StringServiceActionResult execute(UserCommandService usr) {
        boolean b = usr.addUser(payload);

        return transformToWritableResult(b ? "user added" : "add command failed");
    }
}
