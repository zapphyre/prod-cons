package com.indra;

import com.indra.consumer.db.AppSessionFactory;
import com.indra.producer.command.Command;
import com.indra.consumer.CommandConsumer;
import com.indra.consumer.impl.UserDBCommandConsumerImpl;
import com.indra.consumer.db.UserDAO;
import com.indra.producer.CommandProducer;
import com.indra.producer.impl.UserDBCommandProducerImpl;
import com.indra.queue.FIFOQueue;
import com.indra.queue.impl.SyncFIFOQueueImpl;
import com.indra.consumer.service.UserCommandService;
import com.indra.consumer.service.impl.UserCommandDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadingProducerConsumerTest {

    @Test
    void testProduceConsumeReporting() throws InterruptedException {
        FIFOQueue<Command, Boolean> queue = new SyncFIFOQueueImpl(5);
        UserDAO userDAO = new UserDAO(AppSessionFactory.getSessionFactory());

        UserCommandService userCommandService = new UserCommandDataServiceImpl(userDAO);

        CommandProducer producer = new UserDBCommandProducerImpl(queue);
        CommandConsumer consumer = new UserDBCommandConsumerImpl(queue, userCommandService);

        Thread consumerThread = new Thread(consumer);
        Thread producerThread = new Thread(producer);

        producerThread.start();
        consumerThread.start();

        Thread.sleep(5000);

        producer.stop();
        consumer.stop();

        producerThread.interrupt();
        consumerThread.interrupt();

        AppSessionFactory.shutdown();
    }
}
