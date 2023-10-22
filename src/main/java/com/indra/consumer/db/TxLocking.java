package com.indra.consumer.db;

import org.hibernate.Session;

public class TxLocking {
    private Object TX_LOCK = new Object();

    void waitIfTxInProgress(Session session) {
        if (session.getTransaction().isActive())
            waitTxLock();
    }

    void waitTxLock() {
        synchronized (TX_LOCK) {
            try {
                TX_LOCK.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void notifyTxLock() {
        synchronized (TX_LOCK) {
            TX_LOCK.notify();
        }
    }

}
