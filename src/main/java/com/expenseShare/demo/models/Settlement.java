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
    @Column(name = "paidByUser")
    private Users paidByUser;

    @ManyToOne
    @Column(name = "paidToUser")
    private Users paidToUser;

    private Double amount;

    private LocalDateTime createdAt = LocalDateTime.now();
}
