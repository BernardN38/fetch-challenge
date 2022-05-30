package com.fetchbackend.payload;

import lombok.Data;

@Data
public class UserPublicDto {
    private Long id;
    private String username;
    private int pointsAvailable = 0;
}
