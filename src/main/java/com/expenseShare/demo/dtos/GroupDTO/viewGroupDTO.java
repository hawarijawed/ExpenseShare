package com.expenseShare.demo.dtos.GroupDTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class viewGroupDTO {
    private String groupName;
    private String createdBy;
    private String description;
    private LocalDateTime createdAt;
}
