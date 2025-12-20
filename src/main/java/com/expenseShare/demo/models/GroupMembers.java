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
    @Column(name = "group_id")
    private Groups group;

    @ManyToOne
    @Column(name = "user_id")
    private Users user;

    private LocalDateTime createdAt = LocalDateTime.now();

}
