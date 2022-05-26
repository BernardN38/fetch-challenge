package com.fetchbackend.repository;

import com.fetchbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT SUM(t.points) FROM Transaction t WHERE user_id = ?1 ", nativeQuery = true)
    int getUserPointsTotal(Long userId);

    @Query(value = "SELECT SUM(t.points) FROM Transaction t WHERE user_id = ?1 AND payer=?2 ", nativeQuery = true)
    int getUserPointsTotalByPayer(Long userId, String payer);
}
