package com.fetchbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String payer;
    private int points;
    private Date timestamp = new Date();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
