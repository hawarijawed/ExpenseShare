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
    @Column(name = "group")
    private Groups groups;

    @ManyToOne
    @Column(name = "paid_by")
    private Users paidBy;

    @ManyToOne
    @Column(name = "paid_to")
    private Users paidTo;

    private Double paidAmount;
}
