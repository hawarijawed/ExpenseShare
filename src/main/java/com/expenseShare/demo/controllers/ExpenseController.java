package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.ExpenseDTO.CreateExpenseDTO;
import com.expenseShare.demo.dtos.ExpenseDTO.ViewExpenseDTO;
import com.expenseShare.demo.models.Expenses;
import com.expenseShare.demo.services.ExpenseServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpenseServices expenseServices;

    public ExpenseController(ExpenseServices expenseServices){
        this.expenseServices = expenseServices;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@RequestBody CreateExpenseDTO dto){
        //dto.setIdempotencyKey(UUID.randomUUID().toString());
        String res = expenseServices.addExpense(dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId){
        String res = expenseServices.deleteExpense(expenseId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get/{groupId}")
    public ResponseEntity<List<ViewExpenseDTO>> getExpenseByGroup(@PathVariable Long groupId){
        List<ViewExpenseDTO> expenses = expenseServices.getAllExpense(groupId);
        if(expenses.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(expenses, HttpStatus.FOUND);
    }
}
