package com.indra;

import com.indra.command.Command;
import com.indra.command.impl.AddCommand;
import com.indra.consumer.CommandConsumer;
import com.indra.consumer.impl.UserDBCommandConsumer;
import com.indra.db.AppSessionFactory;
import com.indra.db.UserDAO;
import com.indra.model.dto.UserDTO;
import com.indra.model.entity.User;
import com.indra.producer.CommandProducer;
import com.indra.producer.impl.ThreadedCommandProducerImpl;
import com.indra.queue.FIFOQueue;
import com.indra.queue.impl.SyncFIFOQueueImpl;
import com.indra.service.UserCommandService;
import com.indra.service.impl.UserCommandDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ThreadingProducerConsumerTest {

    @Test
    void testProduceConsumeReporting() throws InterruptedException {
        FIFOQueue<Command, Boolean> queue = new SyncFIFOQueueImpl(5);
        UserDAO userDAO = new UserDAO();

        UserCommandService userCommandService = new UserCommandDataServiceImpl(userDAO);

        CommandProducer producer = new ThreadedCommandProducerImpl(queue);
        CommandConsumer consumer = new UserDBCommandConsumer(queue, userCommandService);

        Thread consumerThread = new Thread(consumer);
        Thread producerThread = new Thread(producer);


//        List<Command> cmds = List.of(new AddCommand(prvyUser), new AddCommand(druhyUser));
//        producer.produce(cmds);

        producerThread.start();
        consumerThread.start();

//        List<User> all = userDAO.findAll();
//        producer.produce();

        Thread.sleep(5000);

        producer.stop();
        consumer.stop();

        producerThread.interrupt();
        consumerThread.interrupt();

//        AppSessionFactory.shutdown();
    }
}
