package com.expenseShare.demo.repositories;

import com.expenseShare.demo.models.GroupMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMembers, Long> {
    List<GroupMembers> findByGroups_GroupId(Long groupId);
    boolean existsByGroups_GroupIdAndUser_Id(Long groupId, Long userId);
}
