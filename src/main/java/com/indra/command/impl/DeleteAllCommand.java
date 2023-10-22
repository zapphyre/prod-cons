package com.indra.command.impl;

import com.indra.command.Command;
import com.indra.model.pojo.StringServiceActionResult;
import com.indra.service.UserCommandService;

public class DeleteAllCommand implements Command {

    @Override
    public StringServiceActionResult execute(UserCommandService usr) {
        return transformToWritableResult(usr.deleteAllUsers() ? "delete all complete;" : "deleting FAILED");
    }
}
