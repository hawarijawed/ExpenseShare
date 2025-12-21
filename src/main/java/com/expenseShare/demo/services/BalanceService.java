package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.BalanceDTO.BalanceDTO;
import com.expenseShare.demo.dtos.BalanceDTO.SimplifiedBalanceDTO;
import com.expenseShare.demo.models.Balance;
import com.expenseShare.demo.models.ExpenseSplit;
import com.expenseShare.demo.models.Expenses;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.BalanceRepository;
import com.expenseShare.demo.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final ExpenseRepository expenseRepository;
    public BalanceService(BalanceRepository balanceRepository,
                          ExpenseRepository expenseRepository){
        this.balanceRepository = balanceRepository;
        this.expenseRepository = expenseRepository;
    }

    @Transactional
    public void calculateBalanceForExpense(Expenses expenses){
        Users paidBy = expenses.getPaidBy();

        for(ExpenseSplit split: expenses.getSplits()){
            if(!split.getUser().getId().equals(paidBy.getId())){
                Balance balance = balanceRepository.findByPaidBy_IdAndPaidTo_IdAndGroups_GroupId(
                        split.getUser().getId(), paidBy.getId(), expenses.getGroups().getGroupId()
                ).orElse(new Balance());

                balance.setPaidBy(split.getUser());
                balance.setPaidTo(paidBy);
                balance.setGroups(expenses.getGroups());
                balance.setAmountToPay(balance.getAmountToPay() + split.getAmount());
                balanceRepository.save(balance);
            }
        }
    }

    public List<BalanceDTO> getBalanceList(Long groupId){
        List<Balance> balanceList = balanceRepository.findByGroups_GroupId(groupId);
        List<BalanceDTO> balanceDTOS = new ArrayList<>();

        for(Balance balance: balanceList){
//            Expenses expenses = expenseRepository.findByGroups_GroupIdAndPaidBy_Id(balance.getGroups().getGroupId(),
//                    balance.getPaidBy().getId()).orElseThrow(()->new RuntimeException("No expense found"));

            BalanceDTO dto = new BalanceDTO();
            dto.setBalanceId(balance.getBalanceId());
            dto.setToUserId(balance.getPaidTo().getId());
            dto.setToUserName(balance.getPaidTo().getFullName());

            dto.setFromUserId(balance.getPaidBy().getId());
            dto.setFromUserName(balance.getPaidBy().getFullName());

            dto.setAmount(balance.getAmountToPay());
            dto.setGroupName(balance.getGroups().getGroupName());
            //dto.setDescription(expenses.getDescription()!=null? expenses.getDescription() : "No description");
            balanceDTOS.add(dto);
        }

        return balanceDTOS;
    }

    @Transactional
    public void removeExpenseFromBalance(Expenses expenses){
        Users paidBy = expenses.getPaidBy();

        for(ExpenseSplit split: expenses.getSplits()){
            if(!split.getUser().getId().equals(paidBy.getId())){
                Balance balance = balanceRepository.findByPaidBy_IdAndPaidTo_IdAndGroups_GroupId(
                        split.getUser().getId(),
                        paidBy.getId(),
                        expenses.getGroups().getGroupId()
                ).orElseThrow(()->new RuntimeException("Balance not found"));

                double newAmount = balance.getAmountToPay() - split.getAmount();
                if(newAmount <= 0){
                    balanceRepository.delete(balance);
                }
                else {
                    balance.setAmountToPay(newAmount);
                    balanceRepository.save(balance);
                }
            }
        }
    }

    public List<SimplifiedBalanceDTO> simplifyBalance(Long groupId){
        List<Balance> balanceList = balanceRepository.findByGroups_GroupId(groupId);

        if(balanceList.isEmpty()){
            return null;
        }

        // Net Balance per user
        Map<Users, Double> netMap = new HashMap<>();

        for(Balance b: balanceList){
            netMap.put(b.getPaidBy(), netMap.getOrDefault(b.getPaidBy(),0.0) - b.getAmountToPay());
            netMap.put(b.getPaidTo(), netMap.getOrDefault(b.getPaidTo(),0.0) + b.getAmountToPay());
        }


        //Separate Debtors and Creditors
        Queue<Map.Entry<Users, Double>> debtors = new LinkedList<>();
        Queue<Map.Entry<Users, Double>> creditors = new LinkedList<>();

        for(Map.Entry<Users, Double> entry: netMap.entrySet()){
            if(entry.getValue() < 0.0){
                debtors.add(entry);
            }
            else {
                creditors.add(entry);
            }
        }

        //Simplification
        List<SimplifiedBalanceDTO> result = new ArrayList<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()){
            Map.Entry<Users, Double> debtor = debtors.poll();
            Map.Entry<Users, Double> creditor = creditors.poll();

            double settleAmount = Math.min(Math.abs(debtor.getValue()), creditor.getValue());

            SimplifiedBalanceDTO dto = new SimplifiedBalanceDTO();
            dto.setFromUserId(debtor.getKey().getId());
            dto.setFromUserName(debtor.getKey().getFullName());
            dto.setToUserId(creditor.getKey().getId());
            dto.setToUserName(creditor.getKey().getFullName());
            dto.setAmount(settleAmount);

            result.add(dto);

            debtor.setValue(debtor.getValue() + settleAmount);
            creditor.setValue(creditor.getValue() - settleAmount);

            if(debtor.getValue() < 0){
                debtors.add(debtor);
            }
            if(creditor.getValue() > 0){
                creditors.add(creditor);
            }

        }

        return result;
    }

}
