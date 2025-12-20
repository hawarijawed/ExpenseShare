package com.expenseShare.demo.dtos.AuthenticationDTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
