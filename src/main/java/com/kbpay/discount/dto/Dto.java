package com.kbpay.discount.dto;

import com.kbpay.discount.domain.*;
import lombok.*;
import java.time.*;
import java.util.List;

public class Dto {

    /* ── 공통 응답 ── */
    @Getter @Builder
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        public static <T> ApiResponse<T> ok(T data) {
            return ApiResponse.<T>builder().success(true).message("OK").data(data).build();
        }
        public static <T> ApiResponse<T> ok(String msg, T data) {
            return ApiResponse.<T>builder().success(true).message(msg).data(data).build();
        }
        public static <T> ApiResponse<T> fail(String msg) {
            return ApiResponse.<T>builder().success(false).message(msg).build();
        }
    }

    /* ── 로그인 ── */
    @Getter @Setter @NoArgsConstructor
    public static class LoginRequest { private String userId; private String password; }

    @Getter @Builder
    public static class LoginResponse {
        private String userId; private String grade; private Integer pointBalance; private Boolean isAdmin;
    }

    /* ── 상품 ── */
    @Getter @Builder
    public static class ProductResponse {
        private Long id; private String productName; private String brandName;
        private Integer originalPrice; private Integer discountRate; private Integer discountedPrice;
        private Integer totalCount; private Integer remainingCount;
        private String imageUrl; private String description; private String notice;
        private String category; private String badge;
        private Integer buyers; private Double rating; private Integer reviewCount;
        private Boolean soldOut; private Double soldOutPercent;

        public static ProductResponse from(Product p) {
            int total = p.getTotalCount() != null ? p.getTotalCount() : 1;
            int rem   = p.getRemainingCount() != null ? p.getRemainingCount() : 0;
            return ProductResponse.builder()
                .id(p.getId()).productName(p.getProductName()).brandName(p.getBrandName())
                .originalPrice(p.getOriginalPrice()).discountRate(p.getDiscountRate())
                .discountedPrice(p.getDiscountedPrice()).totalCount(p.getTotalCount())
                .remainingCount(rem).imageUrl(p.getImageUrl()).description(p.getDescription())
                .notice(p.getNotice()).category(p.getCategory()).badge(p.getBadge())
                .buyers(p.getBuyers()).rating(p.getRating()).reviewCount(p.getReviewCount())
                .soldOut(rem <= 0).soldOutPercent(Math.min(100.0,(double)(total-rem)/total*100))
                .build();
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class ProductSaveRequest {
        private String productName; private String brandName; private String category;
        private Integer originalPrice; private Integer discountedPrice;
        private Integer totalCount; private String badge;
        private String imageUrl; private String description; private String notice;
    }

    /* ── 쿠폰 ── */
    @Getter @Setter @NoArgsConstructor
    public static class ApplyRequest { private String userId; private Long productId; }

    @Getter @Builder
    public static class CouponResponse {
        private Long id; private String couponCode; private String productName;
        private String brandName; private String imageUrl;
        private Integer discountAmount; private Integer originalPrice; private Integer discountedPrice;
        private String status; private String issuedAt; private String expiredAt;
        private Long productId;
        private String giftFromUserId;
        private String giftMessage;

        public static CouponResponse from(DiscountCoupon c) {
            return CouponResponse.builder()
                .id(c.getId()).couponCode(c.getCouponCode())
                .productName(c.getProduct().getProductName()).brandName(c.getProduct().getBrandName())
                .imageUrl(c.getProduct().getImageUrl()).discountAmount(c.getDiscountAmount())
                .originalPrice(c.getProduct().getOriginalPrice())
                .discountedPrice(c.getProduct().getDiscountedPrice())
                .status(c.getStatus().name())
                .productId(c.getProduct().getId())
                .issuedAt(c.getIssuedAt() != null ? c.getIssuedAt().toLocalDate().toString() : "")
                .expiredAt(c.getExpiredAt() != null ? c.getExpiredAt().toLocalDate().toString() : "")
                .giftFromUserId(c.getGiftFromUserId())
                .giftMessage(c.getGiftMessage())
                .build();
        }
    }

    /* ── 결제 ── */
    @Getter @Setter @NoArgsConstructor
    public static class PayRequest { private String userId; private String couponCode; private boolean usePoint; }

    @Getter @Builder
    public static class TxResponse {
        private Long id; private String productName; private String brandName;
        private Integer paidAmount; private Integer savedAmount; private Integer originalAmount;
        private Integer pointUsed; private Integer pointEarned;
        private String couponCode; private String status; private String paidAt; private String cancelledAt;

        public static TxResponse from(Transaction t) {
            return TxResponse.builder()
                .id(t.getId()).productName(t.getProduct().getProductName())
                .brandName(t.getProduct().getBrandName())
                .paidAmount(t.getPaidAmount()).savedAmount(t.getSavedAmount())
                .originalAmount(t.getOriginalAmount())
                .pointUsed(t.getPointUsed() != null ? t.getPointUsed() : 0)
                .pointEarned(t.getPointEarned() != null ? t.getPointEarned() : 0)
                .couponCode(t.getCouponCode()).status(t.getStatus().name())
                .paidAt(t.getPaidAt() != null ? t.getPaidAt().toString() : "")
                .cancelledAt(t.getCancelledAt() != null ? t.getCancelledAt().toString() : "")
                .build();
        }
    }

    /* ── 알림 ── */
    @Getter @Builder
    public static class NotifResponse {
        private Long id; private String title; private String description;
        private String type; private Boolean isRead; private String createdAt;

        public static NotifResponse from(Notification n) {
            return NotifResponse.builder()
                .id(n.getId()).title(n.getTitle()).description(n.getDescription())
                .type(n.getType()).isRead(n.getIsRead())
                .createdAt(n.getCreatedAt() != null ? n.getCreatedAt().toString() : "")
                .build();
        }
    }

    /* ── 리뷰 ── */
    @Getter @Setter @NoArgsConstructor
    public static class ReviewRequest { private String userId; private Long productId; private Integer star; private String content; }

    @Getter @Builder
    public static class ReviewResponse {
        private Long id; private String userId; private Integer star; private String content; private String createdAt;
        public static ReviewResponse from(Review r) {
            return ReviewResponse.builder().id(r.getId())
                .userId(r.getUserId().substring(0,Math.min(3,r.getUserId().length()))+"***")
                .star(r.getStar()).content(r.getContent())
                .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().toLocalDate().toString() : "")
                .build();
        }
    }

    /* ── 찜 ── */
    @Getter @Setter @NoArgsConstructor
    public static class WishRequest { private String userId; private Long productId; }

    /* ── 선물 ── */
    @Getter @Setter @NoArgsConstructor
    public static class GiftRequest { private String fromUserId; private String toUserId; private String couponCode; private String message; }

    /* ── MY ── */
    @Getter @Builder
    public static class MyPageResponse {
        private String userId; private String grade; private Integer pointBalance;
        private Boolean isAdmin; private int totalCoupons; private int availCoupons;
        private int usedCoupons; private long totalSaved; private int wishCount;
        private List<CouponResponse> coupons;
    }
}
