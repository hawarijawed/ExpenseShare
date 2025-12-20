package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.ExpenseDTO.CreateExpenseDTO;
import com.expenseShare.demo.services.ExpenseServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpenseServices expenseServices;

    public ExpenseController(ExpenseServices expenseServices){
        this.expenseServices = expenseServices;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@RequestBody CreateExpenseDTO dto){
        String res = expenseServices.addExpense(dto);
        return ResponseEntity.ok(res);
    }
}
