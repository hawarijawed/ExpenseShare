package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.SettlementDTO.SettlementRequestDTO;
import com.expenseShare.demo.dtos.SettlementDTO.ViewSettlementDTO;
import com.expenseShare.demo.models.Settlement;
import com.expenseShare.demo.services.SettlementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/settlement")
public class SettlementController {
    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService){
        this.settlementService = settlementService;
    }


    @PostMapping
    public ResponseEntity<String> settle(@RequestBody SettlementRequestDTO settlementRequestDTO){
        String res = settlementService.settleAmount(settlementRequestDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/history/{groupId}")
    public ResponseEntity<List<ViewSettlementDTO>> history(@PathVariable Long groupId){
        List<ViewSettlementDTO> settlements = settlementService.getSettlementHistory(groupId);
        if(settlements.isEmpty()){
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(settlements, HttpStatus.FOUND);
    }
}
