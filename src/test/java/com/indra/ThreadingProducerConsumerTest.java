package com.indra;

import com.indra.db.AppSessionFactory;
import com.indra.model.dto.UserDTO;
import com.indra.producer.command.Command;
import com.indra.consumer.CommandConsumer;
import com.indra.consumer.impl.UserDBCommandConsumerImpl;
import com.indra.db.repository.UserDAO;
import com.indra.producer.CommandProducer;
import com.indra.producer.impl.UserDBCommandProducerImpl;
import com.indra.queue.FIFOQueue;
import com.indra.queue.impl.SyncFIFOQueueImpl;
import com.indra.service.UserCommandService;
import com.indra.service.impl.UserCommandDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

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

    @Test
    public void hashcodeMapTest() {
        UserDTO dto1 = UserDTO.builder()
                .id(1L)
                .name("name")
                .build();

        UserDTO dto2 = UserDTO.builder()
                .id(1L)
                .name("name")
                .build();

        HashSet<UserDTO> hashSet = new HashSet();

        hashSet.add(dto1);

        boolean contains = hashSet.contains(dto2);

        Assertions.assertTrue(contains);
        HashMap<UserDTO, Integer> hashMap = new HashMap();

        hashMap.put(dto1, 2);

        Integer i = hashMap.get(dto2);

        Assertions.assertEquals(2, i);
    }
}
