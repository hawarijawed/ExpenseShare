package com.expenseShare.demo.repositories;

import com.expenseShare.demo.models.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findByGroup_GroupId(Long groupId);
}
