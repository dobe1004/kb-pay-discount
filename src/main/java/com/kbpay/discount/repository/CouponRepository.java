package com.kbpay.discount.repository;

import com.kbpay.discount.domain.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<DiscountCoupon, Long> {
    // 일반 발급 쿠폰만 중복 체크 (선물 쿠폰 제외)
    boolean existsByUserIdAndProductIdAndGiftFromUserIdIsNull(String userId, Long productId);
    // 기존 메서드 유지 (하위 호환)
    boolean existsByUserIdAndProductId(String userId, Long productId);
    List<DiscountCoupon> findByUserIdOrderByIssuedAtDesc(String userId);
    Optional<DiscountCoupon> findByCouponCode(String couponCode);
}
