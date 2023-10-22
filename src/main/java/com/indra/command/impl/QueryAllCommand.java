package com.indra.command.impl;

import com.indra.command.Command;
import com.indra.model.dto.UserDTO;
import com.indra.model.pojo.StringServiceActionResult;
import com.indra.service.UserCommandService;

import java.util.List;

public class QueryAllCommand implements Command {

    @Override
    public StringServiceActionResult execute(UserCommandService usr) {
        List<UserDTO> allUsers = usr.getAllUsers();

        return transformToWritableResult("query all users", allUsers.size());
    }
}