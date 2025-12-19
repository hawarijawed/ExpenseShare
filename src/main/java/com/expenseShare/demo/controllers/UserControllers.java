package com.expenseShare.demo.controllers;

import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String addUser(@RequestBody Users user){
        return userService.addUser(user);
    }

}
