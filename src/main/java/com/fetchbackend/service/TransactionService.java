package com.fetchbackend.service;

import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.payload.TransactionResponse;

public interface TransactionService {
    TransactionResponse getUserTransactions(int pageNo, int pageSize, String sortBy, String sortDir, Long userId);

    TransactionDto createTransaction(TransactionDto transactionDto, Long userId);

    TransactionDto updateTransaction(TransactionDto transactionDto, Long userId, Long transactionId);

}
