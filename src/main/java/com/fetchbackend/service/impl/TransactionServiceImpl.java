package com.fetchbackend.service.impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ModelMapper mapper;

    public TransactionDto createTransaction(TransactionDto transactionDto){
        Transaction transaction = mapToEntity(transactionDto);
        Transaction newTransaction = transactionRepository.save(transaction);
        return mapToDto(newTransaction);
    }

    private TransactionDto mapToDto(Transaction transaction){
        return  mapper.map(transaction,TransactionDto.class);
    }

    private Transaction mapToEntity(TransactionDto transactionDto){
        return mapper.map(transactionDto,Transaction.class);
    }
}
