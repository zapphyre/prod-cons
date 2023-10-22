package com.indra;

import com.indra.command.Command;
import com.indra.consumer.CommandConsumer;
import com.indra.consumer.impl.UserDBCommandConsumer;
import com.indra.db.AppSessionFactory;
import com.indra.db.UserDAO;
import com.indra.model.dto.UserDTO;
import com.indra.producer.CommandProducer;
import com.indra.producer.impl.ThreadedCommandProducerImpl;
import com.indra.queue.FIFOQueue;
import com.indra.queue.impl.SyncFIFOQueueImpl;
import com.indra.service.UserCommandService;
import com.indra.service.impl.UserCommandDataServiceImpl;

import java.util.UUID;

public class App {
    public static void main(String[] args) {
        FIFOQueue<Command, Boolean> queue = new SyncFIFOQueueImpl(5);
        UserDAO userDAO = new UserDAO();

        UserCommandService userCommandService = new UserCommandDataServiceImpl(userDAO);

        CommandProducer producer = new ThreadedCommandProducerImpl(queue);
        CommandConsumer consumer = new UserDBCommandConsumer(queue, userCommandService);

        Thread consumerThread = new Thread(consumer);
        Thread producerThread = new Thread(producer);

        producerThread.start();
        consumerThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
