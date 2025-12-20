package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.BalanceDTO.BalanceDTO;
import com.expenseShare.demo.models.Balance;
import com.expenseShare.demo.models.ExpenseSplit;
import com.expenseShare.demo.models.Expenses;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.BalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository){
        this.balanceRepository = balanceRepository;
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
            BalanceDTO dto = new BalanceDTO();
            dto.setToUserId(balance.getPaidTo().getId());
            dto.setToUserName(balance.getPaidTo().getFullName());

            dto.setFromUserId(balance.getPaidBy().getId());
            dto.setFromUserName(balance.getPaidBy().getFullName());

            dto.setAmount(balance.getAmountToPay());
            dto.setGroupName(balance.getGroups().getGroupName());

            balanceDTOS.add(dto);
        }

        return balanceDTOS;
    }
}
