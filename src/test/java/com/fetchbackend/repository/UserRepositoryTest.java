package com.fetchbackend.repository;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void itShouldGetUserPointsTotal() {
        //create user
        User user = new User(
                "testUser",
                "password"
        );
        user = userRepository.save(user);

        // create transaction for user
        Transaction transaction = new Transaction("testPayer", 100);
        transaction.setUser(user);
        transactionRepository.save(transaction);

        //when
        Optional<Transaction> sumPoints = userRepository.getUserPointsTotal(user.getId());

        //then
        assertTrue(sumPoints.isPresent());
        assertEquals(sumPoints.get().getPoints(), 100);
    }
}