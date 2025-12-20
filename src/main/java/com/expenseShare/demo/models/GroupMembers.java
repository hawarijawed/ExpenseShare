package com.expenseShare.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_members")
@Data
public class GroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private LocalDateTime createdAt = LocalDateTime.now();

}
