package com.fetchbackend.repository;

import com.fetchbackend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "select exists(select 1 from transaction where user_id = ?1)", nativeQuery = true)
    boolean existsByUserId(Long userId);

    List<Transaction> findByUserId(Long userId);

    Page<Transaction> findPageByUserId(Long userId, Pageable pageable);

    @Query(value = "select * from transaction where user_id = ?1 ORDER BY timestamp", nativeQuery = true)
    List<Transaction> findOrderedByUserId(Long userId);

    @Query(value = "select * from transaction where user_id = ?1 AND points > 0 ORDER BY timestamp LIMIT 1", nativeQuery = true)
    Transaction findOldestByUserId(Long userId);

    @Query(value = "select * from transaction where user_id = ?1 AND points > 0 and timestamp > ?2 ORDER BY timestamp LIMIT 1", nativeQuery = true)
    Transaction findNextOldest(Long id, Date timestamp);
}
