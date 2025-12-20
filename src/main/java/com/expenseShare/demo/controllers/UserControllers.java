package com.expenseShare.demo.controllers;

import com.expenseShare.demo.dtos.userDTOs.RegisterUserDTO;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserControllers {
    private final UserService userService;
    public UserControllers(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/get")
    public ResponseEntity<?> getAll(){
        List<Users> users = userService.getAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<>("No users found", HttpStatus.OK);
        }

        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody @Validated RegisterUserDTO user){
        String res = userService.addUser(user);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        String res = userService.deleteById(id);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAll(){
        String res = userService.deleteAll();
        return ResponseEntity.ok(res);
    }

}
