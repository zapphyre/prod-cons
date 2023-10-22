package com.indra.command.impl;

import com.indra.command.Command;
import com.indra.model.dto.UserDTO;
import com.indra.model.pojo.StringServiceActionResult;
import com.indra.service.UserCommandService;
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
