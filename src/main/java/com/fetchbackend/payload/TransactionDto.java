package com.fetchbackend.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TransactionDto {
    @NotEmpty(message = "Payer should not be null or empty")
    private String payer;
    @NotNull(message = "Points should not be null or empty")
    private int points;
    private Date timestamp = new Date();
}
