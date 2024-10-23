package com.indra;

import com.indra.consumer.CommandConsumer;
import com.indra.db.AppSessionFactory;
import com.indra.db.repository.UserDAO;
import com.indra.consumer.impl.UserDBCommandConsumerImpl;
import com.indra.service.UserCommandService;
import com.indra.service.impl.UserCommandDataServiceImpl;
import com.indra.producer.CommandProducer;
import com.indra.producer.command.Command;
import com.indra.producer.impl.UserDBCommandProducerImpl;
import com.indra.queue.FIFOQueue;
import com.indra.queue.impl.SyncFIFOQueueImpl;

public class App {
    public static void main(String[] args) {
        FIFOQueue<Command, Boolean> queue = new SyncFIFOQueueImpl(5);
        UserDAO userDAO = new UserDAO(AppSessionFactory.getSessionFactory());

        UserCommandService userCommandService = new UserCommandDataServiceImpl(userDAO);

        CommandProducer producer = new UserDBCommandProducerImpl(queue);
        CommandConsumer consumer = new UserDBCommandConsumerImpl(queue, userCommandService);

        Thread consumerThread = new Thread(consumer);
        Thread producerThread = new Thread(producer);

        producerThread.start();
        consumerThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        producer.stop();
        consumer.stop();

        AppSessionFactory.shutdown();
        System.exit(0);
    }
}
