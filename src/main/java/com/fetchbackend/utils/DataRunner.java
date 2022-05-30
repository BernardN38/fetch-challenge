package com.fetchbackend.utils;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataRunner implements CommandLineRunner {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        User testUser = new User();
        testUser.setUsername("tester1");
        testUser.setPassword("password");
        User testUser2 = new User();
        testUser2.setUsername("tester2");
        testUser2.setPassword("password");
        User testUser3 = new User();
        testUser3.setUsername("tester3");
        testUser3.setPassword("password");


        userRepository.saveAll(List.of(testUser, testUser2, testUser3));

        Transaction testTrans5 = new Transaction();
        testTrans5.setPayer("DANNON");
        testTrans5.setPoints(100);
        testTrans5.setUser(testUser);
        transactionRepository.save(testTrans5);

        Transaction testTrans2 = new Transaction();
        testTrans2.setPayer("UNILEVER");
        testTrans2.setPoints(200);
        testTrans2.setUser(testUser);
        transactionRepository.save(testTrans2);

        Transaction testTrans4 = new Transaction();
        testTrans4.setPayer("MILLER COORS");
        testTrans4.setPoints(10000);
        testTrans4.setUser(testUser);
        transactionRepository.save(testTrans4);

        Transaction testTrans1 = new Transaction();
        testTrans1.setPayer("DANNON");
        testTrans1.setPoints(1000);
        testTrans1.setUser(testUser);
        transactionRepository.save(testTrans1);

    }
}
