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
    @JoinColumn(name = "group_id")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Users paidBy;

    @ManyToOne
    @JoinColumn(name = "paid_to")
    private Users paidTo;

    private double AmountToPay=0.0;

    //private boolean isSettled;
}
