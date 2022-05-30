package com.fetchbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@NoArgsConstructor
@AllArgsConstructor
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
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created = Calendar.getInstance();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updated;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction(long points) {
        this.points = (int) points;
    }

    public Transaction(String payer, long points) {
        this.payer = payer;
        this.points = (int) points;
    }


    public Transaction(String payer, int points, Calendar created, User user) {
        this.payer = payer;
        this.points = points;
        this.created = created;
        this.user = user;
    }


    @PrePersist
    protected void onCreate() {
        updated = created = Calendar.getInstance();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Calendar.getInstance();
    }
}
