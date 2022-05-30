package com.fetchbackend.repository;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @BeforeEach
    void setUp() {
        User user = new User(
                1L,
                "testUser",
                "password"
        );
        user = userRepository.save(user);
        Calendar date = Calendar.getInstance();

        Transaction trans1 = new Transaction(1L, "testPayer1", 100, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE,1);
        Transaction trans2 =       new Transaction(2L, "testPayer2", 200, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE, 1);
        Transaction trans3=       new Transaction(3L, "testPayer3", 300, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE, 1);
        Transaction trans4 =     new Transaction(4L, "testPayer1", 400, date, Calendar.getInstance(), user);


        transactionRepository.saveAll(List.of(trans1,trans2,trans3,trans4));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void itShouldFindPageByUserId() {

        //create pageable
        PageRequest pageable = PageRequest.of(0, 3);

        // request page
        Page<Transaction> transactions = transactionRepository.findPageByUserId(1L, pageable);

        assertEquals(transactions.getTotalElements(), 4);
        assertEquals(transactions.getTotalPages(), 2);
        assertEquals(transactions.getNumberOfElements(), 3);

        //create different pageable
        pageable = PageRequest.of(0, 2);

        // request page
        transactions = transactionRepository.findPageByUserId(1L, pageable);
        assertEquals(transactions.getTotalElements(), 4);
        assertEquals(transactions.getTotalPages(), 2);
        assertEquals(transactions.getNumberOfElements(), 2);

    }

    @Test
    void findOldestByUserId() {
        Page<Transaction> transaction = transactionRepository.findOldestByUserId(1L, PageRequest.of(0, 1));
        assertEquals(transaction.getContent().get(0).getPoints(), 100);
    }

    @Test
    void findNextOldest() {
        Page<Transaction> transaction = transactionRepository.findOldestByUserId(1L, PageRequest.of(0, 1));
        assertEquals(transaction.getContent().get(0).getPoints(), 100);
        Page<Transaction> nextTransaction = transactionRepository.findNextOldest(1L, transaction.getContent().get(0).getCreated(), PageRequest.of(0, 1));
        assertEquals(nextTransaction.getContent().get(0).getPoints(), 200);
    }

    @Test
    void findSumPointsPayerByUserId() {
        List<Transaction> pointsTransaction = transactionRepository.findSumPointsPayerByUserId(1L);
        assertEquals(pointsTransaction.get(0).getPoints(), 500);
    }
}
