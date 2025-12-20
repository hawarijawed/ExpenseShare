package com.expenseShare.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "balance")
@Data
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @ManyToOne
    @JoinColumn(name = "group")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Users paidBy;

    @ManyToOne
    @JoinColumn(name = "paid_to")
    private Users paidTo;

    private Double paidAmount;
}
