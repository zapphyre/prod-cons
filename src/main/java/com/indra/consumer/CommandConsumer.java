package com.indra.consumer;

public interface CommandConsumer extends Runnable {
    boolean consume();

    void stop();
}
