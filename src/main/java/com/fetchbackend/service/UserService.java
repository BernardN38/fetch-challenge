package com.fetchbackend.service;

import com.fetchbackend.payload.PointsDto;
import com.fetchbackend.payload.SpendResponse;

import java.util.List;

public interface UserService {
    List<PointsDto> getUserPointsByPayer(Long userId);

    SpendResponse spendUserPoints(Long userId, int amount);

}
