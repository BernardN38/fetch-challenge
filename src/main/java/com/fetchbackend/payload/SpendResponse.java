package com.fetchbackend.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Data
public class SpendResponse {
    private List<PointsDto> deductions = new ArrayList<>();
    private Date timestamp = new Date();
}
