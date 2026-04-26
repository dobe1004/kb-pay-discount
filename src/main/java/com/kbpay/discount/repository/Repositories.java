package com.kbpay.discount.repository;

import com.kbpay.discount.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserIdAndPassword(String userId, String password);
}

@Repository
interface CouponRepository extends JpaRepository<DiscountCoupon, Long> {
    boolean existsByUserIdAndProductId(String userId, Long productId);
    List<DiscountCoupon> findByUserIdOrderByIssuedAtDesc(String userId);
    Optional<DiscountCoupon> findByCouponCode(String couponCode);
}

@Repository
interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByPaidAtDesc(String userId);
}

@Repository
interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    long countByUserIdAndIsReadFalse(String userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.userId = :userId")
    void markAllReadByUserId(@Param("userId") String userId);
}

@Repository
interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    boolean existsByUserIdAndProductId(String userId, Long productId);
}

@Repository
interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Wish> findByUserIdAndProductId(String userId, Long productId);
    boolean existsByUserIdAndProductId(String userId, Long productId);
    void deleteByUserIdAndProductId(String userId, Long productId);
}
