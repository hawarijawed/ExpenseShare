package com.expenseShare.demo.repositories;

import com.expenseShare.demo.models.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Groups, Long> {
    boolean existsByGroupName(String groupName);
}
