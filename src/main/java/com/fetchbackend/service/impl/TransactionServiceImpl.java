package com.fetchbackend.service.impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.exception.FetchApiException;
import com.fetchbackend.exception.ResourceNotFoundException;
import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.payload.TransactionResponse;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import com.fetchbackend.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ModelMapper mapper;

    public TransactionResponse getUserTransactions(int pageNo, int pageSize, String sortBy, String sortDir, Long userId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Transaction> transactions = transactionRepository.findPageByUserId(userId, pageable);
        List<TransactionDto> userTransactions = transactions.getContent().stream().map(this::mapToDto).toList();

        return mapToTransactionResponse(userTransactions, transactions);
    }

    public TransactionDto createTransaction(TransactionDto transactionDto, Long userId) {
        Transaction transaction = mapToEntity(transactionDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new FetchApiException(HttpStatus.NOT_FOUND, "User not Found"));
        transaction.setUser(user);
        Transaction newTransaction = transactionRepository.save(transaction);
        return mapToDto(newTransaction);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto, Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction", "transactionId", transactionId));
        mapper.map(transactionDto, transaction);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDto(updatedTransaction);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        return mapper.map(transaction, TransactionDto.class);
    }

    private Transaction mapToEntity(TransactionDto transactionDto) {
        return mapper.map(transactionDto, Transaction.class);
    }

    private TransactionResponse mapToTransactionResponse(List<TransactionDto> content, Page<Transaction> page) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setContent(content);
        transactionResponse.setPageNo(page.getNumber());
        transactionResponse.setPageSize(page.getSize());
        transactionResponse.setTotalElements(page.getTotalElements());
        transactionResponse.setTotalPages(page.getTotalPages());
        transactionResponse.setLast(page.isLast());
        return transactionResponse;
    }


}
