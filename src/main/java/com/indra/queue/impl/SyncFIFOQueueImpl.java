package com.indra.queue.impl;

import com.indra.producer.command.Command;
import com.indra.queue.FIFOQueue;

import java.util.LinkedList;
import java.util.Queue;

public class SyncFIFOQueueImpl extends BaseSyncQueue implements FIFOQueue<Command, Boolean> {

    private final Queue<Command> queue = new LinkedList<>();

    public SyncFIFOQueueImpl(int maxSize) {
        super(maxSize);
    }

    @Override
    public Boolean enqueue(Command command) throws InterruptedException {
        if (queue.size() >= maxSize)
            waitIsNotFull();

        final boolean res = queue.add(command);

        notifyIsNotEmpty();

        return res;
    }

    @Override
    public Command pollNext() throws InterruptedException {
        if (queue.isEmpty())
            waitIsNotEmpty();

        final Command command = queue.poll();

        notifyIsNotFull();

        return command;
    }

    @Override
    public int size() {
        return queue.size();
    }
}
