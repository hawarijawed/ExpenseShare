package com.expenseShare.demo.dtos.ExpenseDTO;

import lombok.Data;

@Data
public class ViewExpenseDTO {
    private Long expenseId;
    private String description;
    private String groupName;
    private String paidBy;
    private String splitType;
    private Double amount;

}
