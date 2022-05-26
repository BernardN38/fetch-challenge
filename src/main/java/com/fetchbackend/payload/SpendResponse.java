package com.fetchbackend.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

@NoArgsConstructor
@Setter
@Getter
@Data
public class SpendResponse {
    private HashMap<String, Integer> deductions = new HashMap<>();
    private Date timestamp = new Date();
}
