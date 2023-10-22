package com.indra.queue;

public interface FIFOQueue<C, R> {
    R enqueue(C command) throws InterruptedException;

    C pollNext() throws InterruptedException;

    int size();
}
