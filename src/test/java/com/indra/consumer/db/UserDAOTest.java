package com.indra.consumer.db;

import com.indra.consumer.db.UserDAO;
import com.indra.consumer.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

public class UserDAOTest {

    @Test
    void canSaveReadDeleteEntity() {
        UserDAO userDAO = new UserDAO(AppSessionFactory.getSessionFactory().getCurrentSession());

        User u = User.builder()
                .name("user1")
                .build();

        User saved = userDAO.save(u);

        List<User> all = userDAO.findAll();
    }

    @Test
    void shouldAddIdToAnEntityWhenSavedAndReturnIt() {
        MockedStatic<AppSessionFactory> appSessionFactoryMockedStatic = Mockito.mockStatic(AppSessionFactory.class);

        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(transactionMock.isActive()).thenReturn(true);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);
        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(sessionMock);
        Mockito.when(AppSessionFactory.getSessionFactory()).thenReturn(sessionFactory);

        appSessionFactoryMockedStatic.when(AppSessionFactory::getSessionFactory).thenReturn(sessionFactory);



        Assertions.assertTrue(sessionMock.getTransaction().isActive());
    }

    @Test
    void shouldWaitForTxLockObjectToBeReleasedWhenTxIsActive() {
        MockedStatic<AppSessionFactory> appSessionFactoryMockedStatic = Mockito.mockStatic(AppSessionFactory.class);

        UserDAO userDAO = Mockito.mock(UserDAO.class);
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Session sessionMock = Mockito.mock(Session.class);
        Transaction transactionMock = Mockito.mock(Transaction.class);

//        Mockito.when(userDAO.getHibernateSession()).thenReturn(sessionMock);
        Mockito.when(transactionMock.isActive()).thenReturn(true);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);
        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(sessionMock);
//        Mockito.when(AppSessionFactory.getSessionFactory()).thenReturn(sessionFactory);

        appSessionFactoryMockedStatic.when(AppSessionFactory::getSessionFactory).thenReturn(sessionFactory);

        User user = User.builder().build();

        Mockito.when(userDAO.save(user)).thenCallRealMethod();
//        userDAO.save(user);

        Assertions.assertTrue(sessionMock.getTransaction().isActive());
        Mockito.verify(userDAO, Mockito.times(1)).save(user);
    }

    @Test
    void shouldInvokeTxWaitWhenCurrentTxIsActive() {
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionMock) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(transactionMock.isActive()).thenReturn(true);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(Mockito.any());

        Mockito.verify(txLocking, Mockito.times(1)).waitIfTxInProgress(Mockito.any());
    }
    @Test
    void shouldInvokeTxReleaseWhenCurrentTxCompleted() {
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionMock) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(transactionMock.isActive()).thenReturn(false);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(Mockito.any());

        Mockito.verify(txLocking, Mockito.times(1)).notifyTxLock();
    }
    @Test
    void shouldInvokeBeginAndCommitOnTxWhenHappyPath() {
        Session sessionMock = Mockito.mock(Session.class);
        TxLocking txLocking = Mockito.spy(new TxLocking(sessionMock) {});

        Mockito.doNothing().when(txLocking).waitIfTxInProgress(Mockito.any());
        Transaction transactionMock = Mockito.mock(Transaction.class);

        Mockito.when(transactionMock.isActive()).thenReturn(false);
        Mockito.when(sessionMock.getTransaction()).thenReturn(transactionMock);
        Mockito.when(sessionMock.beginTransaction()).thenReturn(transactionMock);

        txLocking.doInTx(s -> s.save(User.builder().build()));

        Mockito.verify(sessionMock, Mockito.times(1)).beginTransaction();
        Mockito.verify(transactionMock, Mockito.times(1)).commit();
    }

}
