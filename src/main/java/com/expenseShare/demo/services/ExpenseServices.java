package com.expenseShare.demo.services;

import com.expenseShare.demo.controllers.ExpenseController;
import com.expenseShare.demo.dtos.ExpenseDTO.CreateExpenseDTO;
import com.expenseShare.demo.dtos.ExpenseDTO.SplitDTO;
import com.expenseShare.demo.dtos.ExpenseDTO.ViewExpenseDTO;
import com.expenseShare.demo.models.*;
import com.expenseShare.demo.repositories.ExpenseRepository;
import com.expenseShare.demo.repositories.GroupRepository;
import com.expenseShare.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpenseServices {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BalanceService balanceService;
    public ExpenseServices(ExpenseRepository expenseRepository,
                           UserRepository userRepository,
                           GroupRepository groupRepository,
                           BalanceService balanceService){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.balanceService = balanceService;
    }

    @Transactional
    public String addExpense(CreateExpenseDTO dto){

        //Checking for duplicate expense entry
        Optional<Expenses> expenses1 = expenseRepository.findByIdempotencyKey(dto.getIdempotencyKey());
        log.info("Expense1 : {}",expenses1);
        if(expenses1.isPresent()){
            return "Duplicate Request. Expense already exists";
        }
        Groups groups = groupRepository.findById(dto.getGroupId()).orElse(null);
        if(groups == null){
            return "Group not found";
        }

        Users paidBy = userRepository.findById(dto.getPaidByUserId()).orElse(null);
        if(paidBy == null){
            return "User not found";
        }

        if(dto.getSplits().isEmpty()){
            return "Splits can not be empty";
        }
        Expenses expenses = new Expenses();
        expenses.setIdempotencyKey(dto.getIdempotencyKey());
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
        balanceService.calculateBalanceForExpense(expenses);
        return "Expense added successfully";
    }

    @Transactional
    public String deleteExpense(Long expenseId){
        Expenses expenses = expenseRepository.findById(expenseId).orElseThrow(()->new RuntimeException("Expense not found"));

        balanceService.removeExpenseFromBalance(expenses);

        expenseRepository.deleteById(expenseId);

        return "Expense Deleted Successfully..";
    }

    public List<ViewExpenseDTO> getAllExpense(Long groupId){
        List<Expenses> expenses = expenseRepository.findByGroups_GroupId(groupId);
        List<ViewExpenseDTO> viewExpenseDTOS = new ArrayList<>();
        for(Expenses exp: expenses){
            ViewExpenseDTO dto = new ViewExpenseDTO();
            dto.setExpenseId(exp.getExpenseId());
            dto.setGroupName(exp.getGroups().getGroupName());
            dto.setPaidBy(exp.getPaidBy().getFullName());
            dto.setSplitType(exp.getSplitType().toString());
            dto.setDescription(exp.getDescription());
            dto.setAmount(exp.getTotalAmount());
            viewExpenseDTOS.add(dto);
        }
        return viewExpenseDTOS;
    }

}
