package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.GroupDTO.AddMemberDTO;
import com.expenseShare.demo.dtos.GroupDTO.CreateGroupDTO;
import com.expenseShare.demo.models.GroupMembers;
import com.expenseShare.demo.models.Groups;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.GroupMemberRepository;
import com.expenseShare.demo.repositories.GroupRepository;
import com.expenseShare.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GroupServices {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    public GroupServices(GroupRepository groupRepository1,
                         UserRepository userRepository,
                         GroupMemberRepository groupMemberRepository){
        this.groupRepository = groupRepository1;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    //Create group
    public String createGroup(CreateGroupDTO dto){
        //Verify creator
        Users creator = userRepository.findById(dto.getCreatedByUserId()).orElseThrow(
                ()-> new RuntimeException("User not found")
        );

        Groups groups = new Groups();
        groups.setGroupName(dto.getGroupName());
        groups.setCreatedBy(creator);

        groupRepository.save(groups);

        //Creator is added as first member
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroups(groups);
        groupMembers.setUser(creator);

        groupMemberRepository.save(groupMembers);

        return "Group created successfully";
    }

    public String addMember(AddMemberDTO dto){
        Groups groups = groupRepository.findById(dto.getGroupId()).orElseThrow(
                ()-> new RuntimeException("Group name not found")
        );

        List<Users> users = userRepository.findAllById(dto.getUserIds());
        if(dto.getUserIds().size() != users.size()){
            return "Invalid user IDs are found Please enter correct data";
        }
        //If user already exist in the group
        for(Users user: users){
            if(!groupMemberRepository.existsByGroups_GroupIdAndUser_Id(dto.getGroupId(), user.getId())){
                GroupMembers groupMembers = new GroupMembers();
                groupMembers.setGroups(groups);
                groupMembers.setUser(user);
                groupMemberRepository.save(groupMembers);
            }
        }
        return "User added to group";
    }

    public List<Users> getGroupMembers(Long groupId){
        List<GroupMembers> groupMembers = groupMemberRepository.findByGroups_GroupId(groupId);
        if (groupMembers.isEmpty()){
            return Collections.emptyList();
        }
        List<Users> memberList = new ArrayList<>();
        for(GroupMembers gm: groupMembers){
            memberList.add(gm.getUser());
        }
        return memberList;
    }
}
