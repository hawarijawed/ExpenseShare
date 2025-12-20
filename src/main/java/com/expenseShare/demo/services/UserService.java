package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.userDTOs.RegisterUserDTO;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String addUser(RegisterUserDTO user){

        if(userRepository.findByEmail(user.getEmail()) != null){
            return "User already exist";
        }
        Users user1 = new Users();
        user1.setFullName(user.getFullName());
        user1.setEmail(user.getEmail());
        if(user.getContact().length() != 10){
            return "Invalid contact number";
        }
        user1.setContact(user.getContact());
        if(user.getPassword().length() < 5){
            return "Choose strong password of length > 5";
        }
        user1.setPassword(user.getPassword());
        userRepository.save(user1);
        return "User added successfully";
    }

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public String deleteById(Long id){
        if(!userRepository.existsById(id)){
            return "User does not exist";
        }
        userRepository.deleteById(id);

        return "User removed!!!";
    }

    public String deleteAll(){
        userRepository.deleteAll();
        return "All users deleted";
    }


}
