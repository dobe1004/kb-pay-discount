package com.kbpay.discount.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "TB_COUPON",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiscountCoupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false) private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") private Product product;

    @Column(unique = true, nullable = false) private String couponCode;
    private Integer discountAmount;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private LocalDateTime expiredAt;

    // 선물받은 쿠폰 정보
    private String giftFromUserId;
    private String giftMessage;

    @PrePersist
    public void prePersist() {
        this.issuedAt = LocalDateTime.now();
        if (this.status == null) this.status = CouponStatus.ISSUED;
        if (this.expiredAt == null) this.expiredAt = LocalDateTime.now().plusDays(30);
    }

    public enum CouponStatus { ISSUED, USED, EXPIRED, GIFTED }
}
