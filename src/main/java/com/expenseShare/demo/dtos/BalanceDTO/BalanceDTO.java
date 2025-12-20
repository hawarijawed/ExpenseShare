package com.expenseShare.demo.dtos.BalanceDTO;

import lombok.Data;

@Data
public class BalanceDTO {
    private String groupName;
    private Long fromUserId;
    private String fromUserName;

    private Long toUserId;
    private String toUserName;

    private double amount;

}
