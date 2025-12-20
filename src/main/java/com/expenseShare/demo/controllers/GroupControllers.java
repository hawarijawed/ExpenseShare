package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.GroupDTO.AddMemberDTO;
import com.expenseShare.demo.dtos.GroupDTO.CreateGroupDTO;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.services.GroupServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupControllers {
    private final GroupServices groupServices;

    public GroupControllers(GroupServices groupServices){
        this.groupServices = groupServices;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createGroup(@RequestBody CreateGroupDTO createGroupDTO){
        String res = groupServices.createGroup(createGroupDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/add-member")
    public ResponseEntity<String> addMember(@RequestBody AddMemberDTO addMemberDTO){
        String res = groupServices.addMember(addMemberDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<Users>> getMembers(@PathVariable Long groupId){
        List<Users> users = groupServices.getGroupMembers(groupId);

        return ResponseEntity.ok(users);
    }
}
