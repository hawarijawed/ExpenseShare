package com.expenseShare.demo.dtos.ExpenseDTO;

import com.expenseShare.demo.models.SplitType;
import lombok.Data;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.List;

@Data
public class CreateExpenseDTO {
    private Long groupId;
    private Long paidByUserId;
    private Double totalAmount;
    private SplitType splitType;
    private String description;
    private List<SplitDTO> splits;
    private String idempotencyKey;
}
