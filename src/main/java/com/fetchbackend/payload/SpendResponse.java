package com.fetchbackend.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
@Data
public class SpendResponse {
    private List<PointsDto> payerDeductions = new ArrayList<>();
    private Date timestamp = new Date();

    public void setPayerDeductions(Map<String, Integer> payerDeductions) {
        List<PointsDto> deductions = new ArrayList<>();
        payerDeductions.forEach((payer, points) ->
                deductions.add(new PointsDto(payer, points))
        );
        this.payerDeductions = deductions;
    }
}
