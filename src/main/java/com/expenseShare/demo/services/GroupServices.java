package com.expenseShare.demo.services;

import com.expenseShare.demo.dtos.GroupDTO.AddMemberDTO;
import com.expenseShare.demo.dtos.GroupDTO.CreateGroupDTO;
import com.expenseShare.demo.dtos.GroupDTO.groupMemberDTO;
import com.expenseShare.demo.dtos.GroupDTO.viewGroupDTO;
import com.expenseShare.demo.models.GroupMembers;
import com.expenseShare.demo.models.Groups;
import com.expenseShare.demo.models.Users;
import com.expenseShare.demo.repositories.GroupMemberRepository;
import com.expenseShare.demo.repositories.GroupRepository;
import com.expenseShare.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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

    @Transactional
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

    public List<viewGroupDTO> viewGroup(){
        List<Groups> groups = groupRepository.findAll();
        List<viewGroupDTO> viewGroupDTOS = new ArrayList<>();
        for(Groups groups1: groups){
            viewGroupDTO grp = new viewGroupDTO();
            grp.setGroupName(groups1.getGroupName());
            grp.setCreatedBy(groups1.getCreatedBy().getFullName());
            grp.setDescription(groups1.getDescription()==null?null:groups1.getDescription());
            grp.setCreatedAt(groups1.getCreatedAt());
            viewGroupDTOS.add(grp);
        }
        return viewGroupDTOS;
    }

    public List<groupMemberDTO> getGroupMembers(Long groupId){
        List<GroupMembers> groupMembers = groupMemberRepository.findByGroups_GroupId(groupId);
        if (groupMembers.isEmpty()){
            return Collections.emptyList();
        }
        List<groupMemberDTO> memberList = new ArrayList<>();
        for(GroupMembers gm: groupMembers){
            groupMemberDTO mm = new groupMemberDTO();
            mm.setFullName(gm.getUser().getFullName());
            mm.setEmail(gm.getUser().getEmail());
            memberList.add(mm);
        }
        return memberList;
    }
}
