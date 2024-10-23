package com.indra.consumer.impl;

import com.indra.producer.command.Command;
import com.indra.consumer.CommandConsumer;
import com.indra.producer.pojo.StringServiceActionResult;
import com.indra.queue.FIFOQueue;
import com.indra.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserDBCommandConsumerImpl implements CommandConsumer {

    private final FIFOQueue<Command, Boolean> fifoQueue;
    private final UserCommandService userCommandService;
    private boolean active = false;

    @Override
    public boolean consume() {
        boolean failed = false;

        while (active) {
            try {
                StringServiceActionResult actionResult = fifoQueue.pollNext().execute(userCommandService);
                log.info(actionResult.reportAll());
                log.info("command executed; queue size: " + fifoQueue.size());
            } catch (InterruptedException e) {
                log.warn("error processing command");
                failed = true;
            }

            if (!active) break;
        }

        return failed;
    }

    @Override
    public void stop() {
        log.info("stopping consumer");
        this.active = false;
    }

    @Override
    public void run() {
        this.active = true;

//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        boolean failed = this.consume();

        log.info(failed ? "consumer encountered errors while processing" : "consuming went well");
    }
}
