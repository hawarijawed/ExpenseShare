package com.expenseShare.demo.dtos.GroupDTO;

import lombok.Data;

@Data
public class CreateGroupDTO {
    private String groupName;
    private Long createdByUserId;
}
