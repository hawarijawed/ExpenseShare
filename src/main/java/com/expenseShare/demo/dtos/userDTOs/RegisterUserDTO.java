package com.expenseShare.demo.dtos.userDTOs;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
@Data
public class RegisterUserDTO {
    @NotNull
    private String fullName;
    @NotNull
    private String email;
    private String contact;
    private String password;

}
