package com.expenseShare.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "expense_split")
public class ExpenseSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseSplitId;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    @JsonBackReference
    private Expenses expense;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Double amount;

    private Double percentage; // nullable
}
