package com.fetchbackend.service;

import com.fetchbackend.payload.TransactionDto;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionDto);
}
