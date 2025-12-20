package com.expenseShare.demo.dtos.ExpenseDTO;

import lombok.Data;

@Data
public class SplitDTO {
    private Long userId;
    private Double amount;
    private Double percentage;
}
