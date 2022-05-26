package com.fetchbackend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PointsDto {
    private String payer;
    private int points = 0;
}
