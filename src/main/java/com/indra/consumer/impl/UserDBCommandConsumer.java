package com.indra.consumer.impl;

import com.indra.command.Command;
import com.indra.consumer.CommandConsumer;
import com.indra.model.pojo.StringServiceActionResult;
import com.indra.queue.FIFOQueue;
import com.indra.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserDBCommandConsumer implements CommandConsumer {

    private final FIFOQueue<Command, Boolean> fifoQueue;
    private final UserCommandService userCommandService;
    private boolean active = false;

    @Override
    public boolean consume() {
        while (active) {
            try {
                Command cmd = fifoQueue.pollNext();
                StringServiceActionResult actionResult = cmd.execute(userCommandService);
                log.info(actionResult.reportAll());
                log.info("command executed; queue size: " + fifoQueue.size());
            } catch (InterruptedException e) {
                log.warn("error processing command");
            }

            if (!active) break;
        }

        return false;
    }

    @Override
    public void stop() {
        log.info("stopping consumer");
        this.active = false;
    }

    @Override
    public void run() {
        this.active = true;

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.consume();
    }
}
