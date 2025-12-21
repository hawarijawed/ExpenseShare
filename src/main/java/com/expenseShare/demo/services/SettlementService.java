package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.SettlementDTO.SettlementRequestDTO;
import com.expenseShare.demo.dtos.SettlementDTO.ViewSettlementDTO;
import com.expenseShare.demo.models.Balance;
import com.expenseShare.demo.models.Settlement;
import com.expenseShare.demo.repositories.BalanceRepository;
import com.expenseShare.demo.repositories.GroupRepository;
import com.expenseShare.demo.repositories.SettlementRepository;
import com.expenseShare.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public SettlementService(SettlementRepository settlementRepository,
                             BalanceRepository balanceRepository,
                             UserRepository userRepository,
                             GroupRepository groupRepository){
        this.settlementRepository = settlementRepository;
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public String settleAmount(SettlementRequestDTO settlementRequestDTO){
        Balance balance = balanceRepository.findByPaidBy_IdAndPaidTo_IdAndGroups_GroupId(
                settlementRequestDTO.getFromUserId(),
                settlementRequestDTO.getToUserId(),
                settlementRequestDTO.getGroupId()
        ).orElse(null);

        if(balance == null){
            return "No balance found";
        }
        if(settlementRequestDTO.getAmount() <= 0){
            return "Settlement amount can not be below 1";
        }
        log.info("Full balance summary: {}", balance);
        log.info("Amount to be paid: {}",balance.getAmountToPay());

        if(balance.getAmountToPay() < 0){
            return "Amount is already settled";
        }
        if(settlementRequestDTO.getAmount() > balance.getAmountToPay()){
            return "Settlement amount exceeds pending balance";
        }

        balance.setAmountToPay(balance.getAmountToPay() - settlementRequestDTO.getAmount());

        if (balance.getAmountToPay() <= 0.0) {
            balanceRepository.save(balance);
            balanceRepository.delete(balance);
        } else {
            balanceRepository.save(balance);
        }
        //Record settlement
        Settlement settlement = new Settlement();
        settlement.setPaidByUser(balance.getPaidBy());
        settlement.setPaidToUser(balance.getPaidTo());
        settlement.setAmount(settlementRequestDTO.getAmount());
        settlement.setGroups(balance.getGroups());

        settlementRepository.save(settlement);

        return "Settlement Successful";
    }

    public List<ViewSettlementDTO> getSettlementHistory(Long groupId){
        List<Settlement> settlements = settlementRepository.findByGroups_GroupId(groupId);

        List<ViewSettlementDTO> settlementDTOS = new ArrayList<>();
        for(Settlement st: settlements){
            ViewSettlementDTO dto = new ViewSettlementDTO();
            dto.setAmountPaid(st.getAmount());
            dto.setFromUserId(st.getPaidByUser().getId());
            dto.setFromUserName(st.getPaidByUser().getFullName());

            dto.setToUserId(st.getPaidToUser().getId());
            dto.setToUserName(st.getPaidToUser().getFullName());
            dto.setGroupName(st.getGroups().getGroupName());

            settlementDTOS.add(dto);
        }

        return settlementDTOS;
    }


}
