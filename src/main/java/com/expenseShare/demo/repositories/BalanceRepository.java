package com.expenseShare.demo.repositories;

import com.expenseShare.demo.models.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByPaidBy_IdAndPaidTo_IdAndGroups_GroupId(
            Long fromId,
            Long toId,
            Long groupId
    );

    List<Balance> findByGroups_GroupId(Long groupId);
}
