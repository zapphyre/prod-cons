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
    public StringServiceActionResult execute(UserCommandService usr) { // really to justify 'I' form SOLID principles, the service should implement 3 different interfaces owned by the producer module (add, query, delete) but I find it sufficient to demonstrate application objective this way now
        boolean b = usr.addUser(payload);

        return transformToWritableResult(b ? "user added" : "add command failed");
    }
}
