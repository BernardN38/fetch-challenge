package com.fetchbackend.controller;

import com.fetchbackend.payload.*;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.service.impl.TransactionServiceImpl;
import com.fetchbackend.service.impl.UserServiceImpl;
import com.fetchbackend.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public UserResponse getUserPoints(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFUALT_PAGE_NUMBER, required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFUALT_PAGE_SIZE, required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return userService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
    }


    //Get user points total by payer
    @GetMapping("{userId}/points")
    public ResponseEntity<Map<String, Integer>> getUserPoints(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserPointsByPayer(userId));
    }

    //Spend user points -> return per payer deduction amounts
    @PostMapping("{userId}/points")
    public SpendResponse spendUserPoints(@Valid @RequestBody SpendDto spendDto, @PathVariable Long userId) {
        return userService.spendUserPoints(userId, spendDto.getPoints());
    }

    @PostMapping
    public UserPublicDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

}
