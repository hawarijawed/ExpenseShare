package com.expenseShare.demo.services;

import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    public UserService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }

    public String addUser(Users user){
        Users user1 = userRepository.findByEmail(user.getEmail());
        if(user1 != null){
            return "User already exist";
        }
        userRepository.save(user);
        return "User addedd successfully";
    }

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }
}
