package com.indra.producer.command.impl;

import com.indra.producer.command.Command;
import com.indra.model.dto.UserDTO;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.consumer.service.UserCommandService;

import java.util.List;

public class QueryAllCommand implements Command {

    @Override
    public StringServiceActionResult execute(UserCommandService usr) {
        List<UserDTO> allUsers = usr.getAllUsers();

        return transformToWritableResult("query all users", allUsers.size());
    }
}
