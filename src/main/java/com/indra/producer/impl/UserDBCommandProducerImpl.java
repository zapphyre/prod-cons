package com.indra.producer.impl;

import com.indra.model.dto.UserDTO;
import com.indra.producer.CommandProducer;
import com.indra.producer.command.Command;
import com.indra.producer.command.impl.AddCommand;
import com.indra.producer.command.impl.DeleteAllCommand;
import com.indra.producer.command.impl.QueryAllCommand;
import com.indra.queue.FIFOQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Slf4j
public class UserDBCommandProducerImpl implements CommandProducer, Runnable {

    private final FIFOQueue<Command, Boolean> fifoQueue;
    private final Queue<Command> preBuffer;
    private boolean active = false;

    public UserDBCommandProducerImpl(FIFOQueue<Command, Boolean> fifoQueue) {
        this.fifoQueue = fifoQueue;
        this.preBuffer = new LinkedList<>();
    }

    @Override
    public void run() {
        this.active = true;

        produce();
    }

    @Override
    public void produce() {

        while (active) {
            try {
                log.debug("queueing new command");

                Command cmd = System.currentTimeMillis() % 2 == 0 ?
                        System.currentTimeMillis() % 3 == 0 ?
                                createDeleteAllCommand() :
                                createQueryCommand() :
                        createAddCommand();

                fifoQueue.enqueue(cmd);
                log.info("command added; queue size: " + fifoQueue.size());
            } catch (InterruptedException e) {
                log.warn("can't wait to enqueue cmd");
            } catch (Exception e) {
                log.error("some serious error while enqueuing the command", e);
            }

            if (!active) break;
        }

    }

    private Command createDeleteAllCommand() {
        return new DeleteAllCommand();
    }

    private Command createAddCommand() {
        UserDTO dto = UserDTO.builder()
                .uuid(UUID.randomUUID())
                .name("User" + Math.random())
                .build();

        return new AddCommand(dto);
    }

    private Command createQueryCommand() {
        return new QueryAllCommand();
    }

    @Override
    public void produce(List<Command> predefined) {
        this.preBuffer.addAll(predefined);
    }

    @Override
    public void stop() {
        log.info("stopping producer");
        this.active = false;
    }
}
