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
    Page<Transaction> findPageByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT t FROM Transaction t  WHERE t.user.id = ?1 AND t.points > 0 ORDER BY t.created")
//    @Query(value = "SELECT * FROM transaction WHERE user_id = ?1 AND points > 0 ORDER BY created LIMIT 1", nativeQuery = true)
    List<Transaction> findOldestByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT t FROM Transaction t  WHERE t.user.id = ?1 AND t.points > 0 AND t.created > ?2 ORDER BY t.created")
//    @Query(value = "SELECT * FROM transaction WHERE user_id = ?1 AND points > 0 and timestamp > ?2 ORDER BY created LIMIT 1", nativeQuery = true)
    List<Transaction> findNextOldest(Long id, Date timestamp, Pageable pageable);

    @Query(value = "SELECT NEW Transaction(t.payer,SUM(t.points)) FROM Transaction t WHERE t.user.id = ?1 GROUP BY t.payer")
    List<Transaction> findSumPointsPayerByUserId(Long userId);
}
