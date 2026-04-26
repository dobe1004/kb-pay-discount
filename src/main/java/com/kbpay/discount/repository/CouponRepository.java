package com.kbpay.discount.repository;

import com.kbpay.discount.domain.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CouponRepository extends JpaRepository<DiscountCoupon, Long> {
    boolean existsByUserIdAndProductId(String userId, Long productId);
    List<DiscountCoupon> findByUserIdOrderByIssuedAtDesc(String userId);
    Optional<DiscountCoupon> findByCouponCode(String couponCode);
}
