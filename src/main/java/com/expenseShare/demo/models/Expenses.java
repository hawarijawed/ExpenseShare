package com.expenseShare.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense")
@Data
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Users paidBy;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;
    private Double totalAmount;
    private String description;
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExpenseSplit> splits = new ArrayList<>();

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;
    private LocalDateTime createdAt = LocalDateTime.now();

}
