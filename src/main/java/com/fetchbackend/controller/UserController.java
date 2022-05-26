package com.fetchbackend.controller;

import com.fetchbackend.payload.PointsDto;
import com.fetchbackend.payload.SpendDto;
import com.fetchbackend.payload.SpendResponse;
import com.fetchbackend.service.impl.TransactionServiceImpl;
import com.fetchbackend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransactionServiceImpl transactionService;

    //Get user points total by payer
    @GetMapping("{userId}/points")
    public List<PointsDto> getUserPoints(@PathVariable Long userId) throws ParseException {
        return userService.getUserPointsByPayer(userId);
    }

    //Spend user points -> return per payer deduction amounts
    @PostMapping("{userId}/points")
    public SpendResponse spendUserPoints(@Valid @RequestBody SpendDto spendDto, @PathVariable Long userId) {
        return userService.spendUserPoints(userId, spendDto.getPoints());
    }

}
