package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.ExpenseDTO.CreateExpenseDTO;
import com.expenseShare.demo.dtos.ExpenseDTO.SplitDTO;
import com.expenseShare.demo.models.*;
import com.expenseShare.demo.repositories.ExpenseRepository;
import com.expenseShare.demo.repositories.GroupRepository;
import com.expenseShare.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseServices {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public ExpenseServices(ExpenseRepository expenseRepository,
                           UserRepository userRepository,
                           GroupRepository groupRepository){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public String addExpense(CreateExpenseDTO dto){
        Groups groups = groupRepository.findById(dto.getGroupId()).orElse(null);
        if(groups == null){
            return "Group not found";
        }

        Users paidBy = userRepository.findById(dto.getPaidByUserId()).orElse(null);
        if(paidBy == null){
            return "User not found";
        }

        Expenses expenses = new Expenses();
        expenses.setGroups(groups);
        expenses.setPaidBy(paidBy);
        expenses.setTotalAmount(dto.getTotalAmount());
        expenses.setSplitType(dto.getSplitType());
        expenses.setDescription(dto.getDescription());

        List<ExpenseSplit> splits = new ArrayList<>();

        if(dto.getSplitType() == SplitType.EQUAL){
            double equalAmount = dto.getTotalAmount()/dto.getSplits().size();

            for(SplitDTO s: dto.getSplits()){
                Users user = userRepository.findById(s.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
                ExpenseSplit split = new ExpenseSplit();
                split.setUser(user);
                split.setAmount(equalAmount);
                split.setExpense(expenses);
                splits.add(split);
            }
        } else if (dto.getSplitType() == SplitType.EXACT) {
            for (SplitDTO s: dto.getSplits()){
                Users user = userRepository.findById(s.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
                ExpenseSplit split = new ExpenseSplit();
                split.setUser(user);
                split.setAmount(s.getAmount());
                split.setExpense(expenses);
                splits.add(split);
            }
        } else if (dto.getSplitType() == SplitType.PERCENTAGE) {
            for(SplitDTO s: dto.getSplits()){
                Users user = userRepository.findById(s.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));
                ExpenseSplit split = new ExpenseSplit();
                split.setUser(user);
                split.setPercentage(s.getPercentage());
                split.setAmount(dto.getTotalAmount()*s.getPercentage()/100);
                split.setExpense(expenses);
                splits.add(split);
            }
        }
        expenses.setSplits(splits);
        expenseRepository.save(expenses);

        return "Expense added successfully";
    }

}
