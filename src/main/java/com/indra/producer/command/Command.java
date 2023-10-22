package com.indra.producer.command;

import com.indra.model.dto.UserDTO;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.consumer.service.UserCommandService;

import java.util.List;

public interface Command {
    StringServiceActionResult execute(UserCommandService usr);

    default StringServiceActionResult transformToWritableResult(String action) {
        StringServiceActionResult stringServiceActionResult = new StringServiceActionResult();
        stringServiceActionResult.addToWriter(action + " complete");

        return stringServiceActionResult;
    }

    default StringServiceActionResult transformToWritableResult(String action, List<UserDTO> users) {
        StringServiceActionResult stringServiceActionResult = new StringServiceActionResult();
        stringServiceActionResult.addToWriter(action + " complete");

        users.forEach(stringServiceActionResult::addToWriter);

        return stringServiceActionResult;
    }

    default StringServiceActionResult transformToWritableResult(String action, int listSize) {
        StringServiceActionResult stringServiceActionResult = new StringServiceActionResult();
        stringServiceActionResult.addToWriter(action + " complete;  " + "listSize is: " + listSize);

        return stringServiceActionResult;
    }
}