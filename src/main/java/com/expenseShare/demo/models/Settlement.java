package com.expenseShare.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "settlement")
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;
    @ManyToOne
    @JoinColumn(name = "paidByUser")
    private Users paidByUser;

    @ManyToOne
    @JoinColumn(name = "paidToUser")
    private Users paidToUser;

    private double amount;

    private LocalDateTime createdAt = LocalDateTime.now();
}
