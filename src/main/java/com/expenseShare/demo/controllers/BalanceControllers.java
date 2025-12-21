package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.BalanceDTO.BalanceDTO;
import com.expenseShare.demo.dtos.BalanceDTO.SimplifiedBalanceDTO;
import com.expenseShare.demo.services.BalanceService;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/balance")
public class BalanceControllers {
    private final BalanceService balanceService;

    public BalanceControllers(BalanceService balanceService){
        this.balanceService = balanceService;
    }

    @GetMapping("/view/{groupId}")
    public ResponseEntity<List<BalanceDTO>> viewBalance(@PathVariable Long groupId){
        List<BalanceDTO> balanceDTOS = balanceService.getBalanceList(groupId);
        return ResponseEntity.ok(balanceDTOS);
    }

    @GetMapping("/simplified/{groupId}")
    public ResponseEntity<List<SimplifiedBalanceDTO>> getSimplifiedBalances(@PathVariable Long groupId){
        List<SimplifiedBalanceDTO> simplifiedBalanceDTOS = balanceService.simplifyBalance(groupId);
        if(simplifiedBalanceDTOS.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(simplifiedBalanceDTOS, HttpStatus.ACCEPTED);
    }
}
