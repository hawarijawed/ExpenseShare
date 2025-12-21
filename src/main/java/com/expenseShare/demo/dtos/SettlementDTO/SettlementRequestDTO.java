package com.expenseShare.demo.dtos.SettlementDTO;

import lombok.Data;

@Data
public class SettlementRequestDTO {
    private Long groupId;
    private Long fromUserId;
    private Long toUserId;
    private double amount;
}
