package com.kbpay.discount.repository;

import com.kbpay.discount.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Wish> findByUserIdAndProductId(String userId, Long productId);
    boolean existsByUserIdAndProductId(String userId, Long productId);
    @Transactional
    void deleteByUserIdAndProductId(String userId, Long productId);
}
