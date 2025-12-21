package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.BalanceDTO.BalanceDTO;
import com.expenseShare.demo.dtos.BalanceDTO.SimplifiedBalanceDTO;
import com.expenseShare.demo.models.Balance;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.BalanceRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BalanceSimplificationService {
    private final BalanceRepository balanceRepository;

    public BalanceSimplificationService(BalanceRepository balanceRepository){
        this.balanceRepository = balanceRepository;
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
            netMap.put(b.getPaidTo(), netMap.getOrDefault(b.getPaidTo(),0.0)+b.getAmountToPay());
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
            dto.setFromUserId(creditor.getKey().getId());
            dto.setFromUserName(creditor.getKey().getFullName());
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
