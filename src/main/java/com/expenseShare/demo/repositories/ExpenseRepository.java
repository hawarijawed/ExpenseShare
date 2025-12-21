package com.expenseShare.demo.repositories;

import com.expenseShare.demo.models.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findByGroups_GroupId(Long groupId);
    Optional<Expenses> findByGroups_GroupIdAndPaidBy_Id(Long groupId, Long Id);
    Optional<Expenses> findByIdempotencyKey(String idempotencyKey);
}
