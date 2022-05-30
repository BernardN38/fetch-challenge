package com.fetchbackend.payload;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private List<UserPublicDto> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;
}
