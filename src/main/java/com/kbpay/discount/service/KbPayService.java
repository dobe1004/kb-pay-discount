package com.kbpay.discount.service;

import com.kbpay.discount.domain.*;
import com.kbpay.discount.dto.Dto.*;
import com.kbpay.discount.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KbPayService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;
    private final TransactionRepository txRepo;
    private final NotificationRepository notifRepo;
    private final ReviewRepository reviewRepo;
    private final WishRepository wishRepo;

    /* ── 로그인 ── */
    @Transactional(readOnly = true)
    public ApiResponse<LoginResponse> login(LoginRequest req) {
        return userRepo.findByUserIdAndPassword(req.getUserId(), req.getPassword())
            .map(u -> ApiResponse.ok(LoginResponse.builder()
                .userId(u.getUserId()).grade(u.getGrade())
                .pointBalance(u.getPointBalance()).isAdmin(u.getIsAdmin()).build()))
            .orElse(ApiResponse.fail("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    /* ── 상품 목록 ── */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return productRepo.findByIsActiveTrueOrderByCreatedAtDesc()
            .stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        return productRepo.findById(id).map(ProductResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    /* ── 관리자: 상품 추가/수정/삭제 ── */
    @Transactional
    public ApiResponse<ProductResponse> saveProduct(ProductSaveRequest req, Long editId) {
        Product p;
        int rate = (int)Math.round((double)(req.getOriginalPrice()-req.getDiscountedPrice())/req.getOriginalPrice()*100);
        if (editId != null) {
            p = productRepo.findById(editId).orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        } else {
            p = new Product();
            p.setRemainingCount(req.getTotalCount());
        }
        p.setProductName(req.getProductName()); p.setBrandName(req.getBrandName());
        p.setCategory(req.getCategory()); p.setOriginalPrice(req.getOriginalPrice());
        p.setDiscountedPrice(req.getDiscountedPrice()); p.setDiscountRate(rate);
        p.setTotalCount(req.getTotalCount()); p.setBadge(req.getBadge());
        p.setImageUrl(req.getImageUrl()); p.setDescription(req.getDescription());
        p.setNotice(req.getNotice()); p.setIsActive(true);
        productRepo.save(p);
        return ApiResponse.ok(editId == null ? "상품이 추가되었습니다." : "상품이 수정되었습니다.", ProductResponse.from(p));
    }

    @Transactional
    public ApiResponse<Void> deleteProduct(Long id) {
        Product p = productRepo.findById(id).orElseThrow();
        p.setIsActive(false);
        productRepo.save(p);
        return ApiResponse.ok("삭제되었습니다.", null);
    }

    /* ── 쿠폰 발급 (선착순) ── */
    @Transactional
    public synchronized ApiResponse<CouponResponse> applyCoupon(ApplyRequest req) {
        try {
            if (couponRepo.existsByUserIdAndProductId(req.getUserId(), req.getProductId()))
                return ApiResponse.fail("이미 발급받은 쿠폰입니다.");

            Product p = productRepo.findByIdWithLock(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            if (!p.hasRemaining()) return ApiResponse.fail("선착순이 마감되었습니다.");

            p.decreaseRemaining();
            productRepo.save(p);

            String code = "KB-" + req.getProductId() + "-"
                + req.getUserId().substring(0, Math.min(4, req.getUserId().length())).toUpperCase()
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            DiscountCoupon coupon = DiscountCoupon.builder()
                .userId(req.getUserId()).product(p).couponCode(code)
                .discountAmount(p.getOriginalPrice() - p.getDiscountedPrice())
                .status(DiscountCoupon.CouponStatus.ISSUED).build();
            couponRepo.save(coupon);

            return ApiResponse.ok("쿠폰이 발급되었습니다!", CouponResponse.from(coupon));
        } catch (OptimisticLockingFailureException e) {
            return ApiResponse.fail("잠시 후 다시 시도해주세요.");
        }
    }

    /* ── 쿠폰함 조회 ── */
    @Transactional(readOnly = true)
    public ApiResponse<MyPageResponse> getMyCoupons(String userId) {
        User user = userRepo.findById(userId).orElseThrow();
        List<DiscountCoupon> coupons = couponRepo.findByUserIdOrderByIssuedAtDesc(userId);
        List<CouponResponse> list = coupons.stream().map(CouponResponse::from).collect(Collectors.toList());
        long totalSaved = txRepo.findByUserIdOrderByPaidAtDesc(userId).stream()
            .filter(t -> t.getStatus() == Transaction.TxStatus.PAID)
            .mapToLong(Transaction::getSavedAmount).sum();
        return ApiResponse.ok(MyPageResponse.builder()
            .userId(userId).grade(user.getGrade()).pointBalance(user.getPointBalance())
            .isAdmin(user.getIsAdmin())
            .totalCoupons(coupons.size())
            .availCoupons((int) coupons.stream().filter(c -> c.getStatus() == DiscountCoupon.CouponStatus.ISSUED).count())
            .usedCoupons((int) coupons.stream().filter(c -> c.getStatus() == DiscountCoupon.CouponStatus.USED).count())
            .totalSaved(totalSaved).wishCount((int) wishRepo.findByUserIdOrderByCreatedAtDesc(userId).size())
            .coupons(list).build());
    }

    /* ── 결제 ── */
    @Transactional
    public ApiResponse<TxResponse> pay(PayRequest req) {
        DiscountCoupon coupon = couponRepo.findByCouponCode(req.getCouponCode())
            .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));
        if (!coupon.getUserId().equals(req.getUserId()))
            return ApiResponse.fail("본인 쿠폰만 사용 가능합니다.");
        if (coupon.getStatus() != DiscountCoupon.CouponStatus.ISSUED)
            return ApiResponse.fail("이미 사용되었거나 만료된 쿠폰입니다.");

        User user = userRepo.findById(req.getUserId()).orElseThrow();
        int discountedPrice = coupon.getProduct().getDiscountedPrice();
        int pointUsed = 0;

        if (req.isUsePoint() && user.getPointBalance() >= 100) {
            pointUsed = Math.min(user.getPointBalance(), discountedPrice);
            user.setPointBalance(user.getPointBalance() - pointUsed);
        }

        int finalAmount = discountedPrice - pointUsed;
        int earnedPoint = (int)(finalAmount * 0.01);
        user.setPointBalance(user.getPointBalance() + earnedPoint);

        // 등급 업데이트
        List<Transaction> allTx = txRepo.findByUserIdOrderByPaidAtDesc(req.getUserId());
        long totalSpent = allTx.stream().filter(t->t.getStatus()==Transaction.TxStatus.PAID)
            .mapToLong(Transaction::getPaidAmount).sum() + finalAmount;
        user.setGrade(totalSpent >= 500000 ? "VIP" : totalSpent >= 200000 ? "골드" : totalSpent >= 50000 ? "실버" : "일반");
        userRepo.save(user);

        coupon.setStatus(DiscountCoupon.CouponStatus.USED);
        coupon.setUsedAt(LocalDateTime.now());
        couponRepo.save(coupon);

        Transaction tx = Transaction.builder()
            .userId(req.getUserId()).product(coupon.getProduct()).couponCode(req.getCouponCode())
            .paidAmount(finalAmount).savedAmount(coupon.getDiscountAmount())
            .originalAmount(coupon.getProduct().getOriginalPrice())
            .pointUsed(pointUsed).pointEarned(earnedPoint)
            .status(Transaction.TxStatus.PAID).build();
        txRepo.save(tx);

        saveNotif(req.getUserId(), "💳 결제 완료",
            finalAmount + "원 결제 완료! +" + earnedPoint + "P 적립", "g");

        return ApiResponse.ok("결제가 완료되었습니다!", TxResponse.from(tx));
    }

    /* ── 결제 취소 ── */
    @Transactional
    public ApiResponse<TxResponse> cancelPay(Long txId, String userId) {
        Transaction tx = txRepo.findById(txId).orElseThrow();
        if (!tx.getUserId().equals(userId)) return ApiResponse.fail("본인 결제만 취소 가능합니다.");
        if (tx.getStatus() != Transaction.TxStatus.PAID) return ApiResponse.fail("이미 취소된 내역입니다.");

        tx.setStatus(Transaction.TxStatus.CANCELLED);
        tx.setCancelledAt(LocalDateTime.now());
        txRepo.save(tx);

        // 쿠폰 복원
        couponRepo.findByCouponCode(tx.getCouponCode()).ifPresent(c -> {
            c.setStatus(DiscountCoupon.CouponStatus.ISSUED);
            c.setUsedAt(null);
            couponRepo.save(c);
        });

        // 포인트 복원
        User user = userRepo.findById(userId).orElseThrow();
        if (tx.getPointEarned() != null) user.setPointBalance(user.getPointBalance() - tx.getPointEarned());
        if (tx.getPointUsed() != null)   user.setPointBalance(user.getPointBalance() + tx.getPointUsed());
        userRepo.save(user);

        saveNotif(userId, "취소 완료", "결제가 취소되고 쿠폰이 복원되었습니다.", "r");
        return ApiResponse.ok("취소되었습니다.", TxResponse.from(tx));
    }

    /* ── 이용내역 ── */
    @Transactional(readOnly = true)
    public List<TxResponse> getHistory(String userId) {
        return txRepo.findByUserIdOrderByPaidAtDesc(userId)
            .stream().map(TxResponse::from).collect(Collectors.toList());
    }

    /* ── 찜 토글 ── */
    @Transactional
    public ApiResponse<Boolean> toggleWish(WishRequest req) {
        if (wishRepo.existsByUserIdAndProductId(req.getUserId(), req.getProductId())) {
            wishRepo.deleteByUserIdAndProductId(req.getUserId(), req.getProductId());
            return ApiResponse.ok("찜이 해제되었습니다.", false);
        } else {
            Product p = productRepo.findById(req.getProductId()).orElseThrow();
            wishRepo.save(Wish.builder().userId(req.getUserId()).product(p).build());
            return ApiResponse.ok("찜했습니다!", true);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getWishIds(String userId) {
        return wishRepo.findByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(w -> w.getProduct().getId()).collect(Collectors.toList());
    }

    /* ── 알림 ── */
    @Transactional(readOnly = true)
    public ApiResponse<Map<String,Object>> getNotifs(String userId) {
        List<NotifResponse> list = notifRepo.findByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(NotifResponse::from).collect(Collectors.toList());
        long unread = notifRepo.countByUserIdAndIsReadFalse(userId);
        Map<String,Object> result = new HashMap<>();
        result.put("notifications", list);
        result.put("unreadCount", unread);
        return ApiResponse.ok(result);
    }

    @Transactional
    public void readAllNotifs(String userId) {
        notifRepo.markAllReadByUserId(userId);
    }

    /* ── 리뷰 ── */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long productId) {
        return reviewRepo.findByProductIdOrderByCreatedAtDesc(productId)
            .stream().map(ReviewResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public ApiResponse<ReviewResponse> saveReview(ReviewRequest req) {
        if (req.getContent() == null || req.getContent().trim().length() < 10)
            return ApiResponse.fail("후기를 10자 이상 작성해주세요.");
        Product p = productRepo.findById(req.getProductId()).orElseThrow();
        Review r = Review.builder().userId(req.getUserId()).product(p)
            .star(req.getStar()).content(req.getContent().trim()).build();
        reviewRepo.save(r);
        return ApiResponse.ok("후기가 등록되었습니다!", ReviewResponse.from(r));
    }

    /* ── 선물 ── */
    @Transactional
    public ApiResponse<CouponResponse> giftCoupon(GiftRequest req) {
        if (req.getFromUserId().equals(req.getToUserId())) return ApiResponse.fail("본인에게는 선물할 수 없습니다.");
        if (!userRepo.existsById(req.getToUserId())) return ApiResponse.fail("존재하지 않는 아이디입니다.");

        DiscountCoupon original = couponRepo.findByCouponCode(req.getCouponCode()).orElseThrow();
        if (!original.getUserId().equals(req.getFromUserId())) return ApiResponse.fail("본인 쿠폰만 선물 가능합니다.");
        if (original.getStatus() != DiscountCoupon.CouponStatus.ISSUED) return ApiResponse.fail("사용 불가한 쿠폰입니다.");

        original.setStatus(DiscountCoupon.CouponStatus.GIFTED);
        couponRepo.save(original);

        String newCode = "KB-GIFT-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        DiscountCoupon gift = DiscountCoupon.builder()
            .userId(req.getToUserId()).product(original.getProduct()).couponCode(newCode)
            .discountAmount(original.getDiscountAmount()).giftFromUserId(req.getFromUserId())
            .giftMessage(req.getMessage())
            .status(DiscountCoupon.CouponStatus.ISSUED).build();
        couponRepo.save(gift);

        saveNotif(req.getToUserId(), "🎁 선물 도착!", req.getFromUserId()+"님이 쿠폰을 선물했습니다!", "y");
        saveNotif(req.getFromUserId(), "🎁 선물 발송 완료", req.getToUserId()+"님께 쿠폰을 선물했습니다!", "y");
        return ApiResponse.ok("선물을 보냈습니다!", CouponResponse.from(gift));
    }

    /* ── 내부 헬퍼 ── */
    private void saveNotif(String userId, String title, String desc, String type) {
        try {
            notifRepo.save(Notification.builder().userId(userId).title(title).description(desc).type(type).build());
        } catch (Exception e) { log.error("알림 저장 실패: {}", e.getMessage()); }
    }
}
