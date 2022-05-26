package com.fetchbackend.payload;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    @NotEmpty(message = "Payer Should not be null or empty")
    private String payer;
    @NotNull(message = "Points should not be null or empty")
    @Positive
    private int points;
    private Date timestamp = new Date();
}
