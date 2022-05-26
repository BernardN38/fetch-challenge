package com.fetchbackend.controller;

import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.payload.TransactionResponse;
import com.fetchbackend.service.impl.TransactionServiceImpl;
import com.fetchbackend.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users/")
public class TransactionController {
    @Autowired
    private TransactionServiceImpl transactionService;

    //get all user transactions by userId with option to page and sort
    @GetMapping("{userId}/transactions")
    public TransactionResponse getAllTransactionsByUserId(@PathVariable Long userId,
                                                          @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFUALT_PAGE_NUMBER, required = false) int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFUALT_PAGE_SIZE, required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return transactionService.getUserTransactions(pageNo, pageSize, sortBy, sortDir, userId);
    }


    // create transaction
    @PostMapping("{userId}/transactions")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto, @PathVariable Long userId) {
        TransactionDto responseDto;
        responseDto = transactionService.createTransaction(transactionDto, userId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //update transaction
    @PutMapping("{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransaction(@Valid @RequestBody TransactionDto transactionDto, @PathVariable Long userId, @PathVariable Long transactionId) {
        return ResponseEntity.ok().body(transactionService.updateTransaction(transactionDto, userId, transactionId));
    }

}
