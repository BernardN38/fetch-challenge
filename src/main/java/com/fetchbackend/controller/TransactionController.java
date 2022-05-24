package com.fetchbackend.controller;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.service.TransactionService;
import com.fetchbackend.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto){
        return new ResponseEntity<>(transactionService.createTransaction(transactionDto), HttpStatus.CREATED);

    }


}
