package com.fetchbackend.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class SpendDto {
    @Positive
    @NotNull
    private int points;
}
