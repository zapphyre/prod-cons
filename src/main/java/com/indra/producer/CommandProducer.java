package com.indra.producer;

import com.indra.command.Command;

import java.util.List;

public interface CommandProducer extends Runnable {
    void produce();

    void produce(List<Command> predefined);

    void stop();
}
