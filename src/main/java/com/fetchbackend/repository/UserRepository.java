package com.fetchbackend.repository;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT NEW Transaction(SUM(t.points)) FROM Transaction t WHERE t.user.id = ?1")
    Optional<Transaction> getUserPointsTotal(Long userId);
}
