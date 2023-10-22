package com.indra.model.pojo;

import com.indra.model.dto.UserDTO;

public class StringServiceActionResult {

    private final StringBuilder resultBuilder = new StringBuilder();

    public void addToWriter(String res) {
        this.resultBuilder.append(res);
    }

    public void addToWriter(UserDTO res) {
        this.resultBuilder.append(res.toString());
    }

    public String reportAll() {
        return resultBuilder.toString();
    }
}
