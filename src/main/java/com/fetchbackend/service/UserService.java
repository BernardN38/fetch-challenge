package com.fetchbackend.service;

import com.fetchbackend.entity.User;
import com.fetchbackend.payload.SpendResponse;
import com.fetchbackend.payload.UserDto;
import com.fetchbackend.payload.UserPublicDto;
import com.fetchbackend.payload.UserResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Integer> getUserPointsByPayer(Long userId);

    SpendResponse spendUserPoints(Long userId, int amount);

    UserPublicDto createUser(UserDto userDto);

    UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

}
