package com.indra.queue.impl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseSyncQueue {
    protected final int maxSize;
    private final Object IS_NOT_FULL = new Object();
    private final Object IS_NOT_EMPTY = new Object();

    public BaseSyncQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void waitIsNotFull() throws InterruptedException {
        synchronized (IS_NOT_FULL) {
            log.debug("queue overwhelmed, waiting");
            IS_NOT_FULL.wait();
            log.debug("queue free again");
        }
    }

    protected void notifyIsNotFull() {
        synchronized (IS_NOT_FULL) {
            log.debug("notifying not full");
            IS_NOT_FULL.notify();
        }
    }

    public void waitIsNotEmpty() throws InterruptedException {
        synchronized (IS_NOT_EMPTY) {
            log.debug("waiting not empty");
            IS_NOT_EMPTY.wait();
        }
    }

    public void notifyIsNotEmpty() {
        synchronized (IS_NOT_EMPTY) {
            log.debug("notifying not empty");
            IS_NOT_EMPTY.notify();
        }
    }
}
