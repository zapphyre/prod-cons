package com.indra.producer.command.impl;

import com.indra.producer.command.Command;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.consumer.service.UserCommandService;

public class DeleteAllCommand implements Command {

    @Override
    public StringServiceActionResult execute(UserCommandService usr) {
        return transformToWritableResult("deleted " + usr.deleteAllUsers() + " results");
    }
}
