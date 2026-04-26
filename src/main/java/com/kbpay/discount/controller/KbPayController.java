package com.kbpay.discount.controller;

import com.kbpay.discount.dto.Dto.*;
import com.kbpay.discount.service.KbPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KbPayController {

    private final KbPayService svc;

    /* ── 로그인 ── */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(svc.login(req));
    }

    /* ── 상품 ── */
    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(ApiResponse.ok(svc.getProducts()));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(svc.getProduct(id)));
    }

    /* ── 관리자: 상품 추가/수정/삭제 ── */
    @PostMapping("/admin/products")
    public ResponseEntity<?> addProduct(@RequestBody ProductSaveRequest req) {
        return ResponseEntity.ok(svc.saveProduct(req, null));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody ProductSaveRequest req) {
        return ResponseEntity.ok(svc.saveProduct(req, id));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(svc.deleteProduct(id));
    }

    /* ── 쿠폰 발급 ── */
    @PostMapping("/coupons/apply")
    public ResponseEntity<?> apply(@RequestBody ApplyRequest req) {
        return ResponseEntity.ok(svc.applyCoupon(req));
    }

    /* ── 쿠폰함 / MY ── */
    @GetMapping("/my/{userId}")
    public ResponseEntity<?> my(@PathVariable String userId) {
        return ResponseEntity.ok(svc.getMyCoupons(userId));
    }

    /* ── 결제 ── */
    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody PayRequest req) {
        return ResponseEntity.ok(svc.pay(req));
    }

    @PostMapping("/pay/{txId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long txId, @RequestParam String userId) {
        return ResponseEntity.ok(svc.cancelPay(txId, userId));
    }

    /* ── 이용내역 ── */
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> history(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(svc.getHistory(userId)));
    }

    /* ── 찜 ── */
    @PostMapping("/wish")
    public ResponseEntity<?> toggleWish(@RequestBody WishRequest req) {
        return ResponseEntity.ok(svc.toggleWish(req));
    }

    @GetMapping("/wish/{userId}")
    public ResponseEntity<?> getWishes(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(svc.getWishIds(userId)));
    }

    /* ── 알림 ── */
    @GetMapping("/notif/{userId}")
    public ResponseEntity<?> getNotifs(@PathVariable String userId) {
        return ResponseEntity.ok(svc.getNotifs(userId));
    }

    @PostMapping("/notif/{userId}/read-all")
    public ResponseEntity<?> readAll(@PathVariable String userId) {
        svc.readAllNotifs(userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /* ── 리뷰 ── */
    @GetMapping("/reviews/{productId}")
    public ResponseEntity<?> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.ok(svc.getReviews(productId)));
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> saveReview(@RequestBody ReviewRequest req) {
        return ResponseEntity.ok(svc.saveReview(req));
    }

    /* ── 선물 ── */
    @PostMapping("/gift")
    public ResponseEntity<?> gift(@RequestBody GiftRequest req) {
        return ResponseEntity.ok(svc.giftCoupon(req));
    }

    /* ── 예외 처리 ── */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleError(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponse.fail("서버 오류: " + e.getMessage()));
    }
}
