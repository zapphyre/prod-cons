package com.indra.consumer.db;

import com.indra.consumer.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class TxLockingTest {

    @Test
    void shouldInvokeTxWaitWhenCurrentTxIsActive() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionFactory) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(sessionMock);
        Mockito.when(transactionMock.isActive()).thenReturn(true);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(Mockito.any());

        Mockito.verify(txLocking, Mockito.times(1)).waitIfTxInProgress(Mockito.any());
    }

    @Test
    void shouldInvokeTxReleaseWhenCurrentTxCompleted() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionFactory) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(sessionMock);
        Mockito.when(transactionMock.isActive()).thenReturn(false);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(Mockito.any());

        Mockito.verify(txLocking, Mockito.times(1)).notifyTxLock();
    }

    @Test
    void shouldInvokeBeginAndCommitOnTxWhenHappyPath() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionFactory) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(sessionMock);
        Mockito.when(transactionMock.isActive()).thenReturn(false);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);
        Mockito.when(sessionMock.beginTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(s -> s.save(User.builder().build()));

        Mockito.verify(sessionMock, Mockito.times(1)).beginTransaction();
        Mockito.verify(transactionMock, Mockito.times(1)).commit();
    }

}
