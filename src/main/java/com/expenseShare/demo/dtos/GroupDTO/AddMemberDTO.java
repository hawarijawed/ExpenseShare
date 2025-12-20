package com.expenseShare.demo.dtos.GroupDTO;

import lombok.Data;

import java.util.List;

@Data
public class AddMemberDTO {
    private Long groupId;
    private List<Long> userIds;
}
