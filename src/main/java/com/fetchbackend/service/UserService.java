package com.fetchbackend.service;

import com.fetchbackend.payload.SpendResponse;

import java.util.Map;

public interface UserService {
    Map<String, Integer> getUserPointsByPayer(Long userId);

    SpendResponse spendUserPoints(Long userId, int amount);

}
