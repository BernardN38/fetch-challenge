package com.fetchbackend.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String payer;
    private int points;
    private Date timestamp;

}
