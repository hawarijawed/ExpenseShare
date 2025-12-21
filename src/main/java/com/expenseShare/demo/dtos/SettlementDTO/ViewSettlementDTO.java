package com.expenseShare.demo.dtos.SettlementDTO;

import lombok.Data;

@Data
public class ViewSettlementDTO {
    private String groupName;
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    private Double amountPaid;
}
