package com.expenseShare.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Data
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @ManyToOne
    @Column(name = "group")
    private Groups groups;

    @ManyToOne
    @Column(name = "paid_by")
    private Users user;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;
    private Double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();

}
