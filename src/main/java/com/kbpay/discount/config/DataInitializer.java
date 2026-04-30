package com.kbpay.discount.config;

import com.kbpay.discount.domain.*;
import com.kbpay.discount.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    @Override
    public void run(String... args) {
        initUsers();
        initProducts();
        log.info("✅ KB Pay 데이터 초기화 완료!");
    }

    private void initUsers() {
        if (userRepo.count() > 0) return;

        // admin
        userRepo.save(User.builder().userId("admin").password("admin1234")
            .grade("VIP").pointBalance(10000).isAdmin(true).build());

        // user01 ~ user10
        for (int i = 1; i <= 10; i++) {
            String uid = String.format("user%02d", i);
            userRepo.save(User.builder().userId(uid).password("1234")
                .grade("일반").pointBalance(0).isAdmin(false).build());
        }
        log.info("👤 계정 11개 생성 완료 (admin + user01~user10)");
    }

    private void initProducts() {
        if (productRepo.count() > 0) return;

        List<Product> products = List.of(
            build("스타벅스 아메리카노 Tall 10잔","STARBUCKS",65000,39000,40,100,"카페","hot",1243,4.8,89,
                "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=400&q=80",
                "☕ KB페이 단독 특가! 정가 65,000원 → 39,000원\n\n전국 스타벅스 매장 아메리카노 Tall 10잔 이용권\n\n✅ 아이스/따뜻한 아메리카노 Tall 사이즈\n✅ 발급일로부터 60일 유효\n✅ 전국 스타벅스 전 매장 사용 가능",
                "• 모바일 쿠폰 특성상 환불 불가\n• 유효기간 내 미사용 시 자동 소멸", 23, 60),
            build("투썸플레이스 케이크+음료 세트","A TWOSOME PLACE",32000,19200,40,80,"카페","new",456,4.6,34,
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&q=80",
                "🎂 정가 32,000원 → 19,200원 (40% 할인)\n\n투썸플레이스 인기 케이크(조각) 1개 + 음료 1잔 세트\n\n✅ 전국 투썸플레이스 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 매장 재고에 따라 케이크 품목 변동 가능\n• 조각케이크에 한함", 11, 30),
            build("메가커피 아메리카노 20잔","MEGA COFFEE",30000,18000,40,150,"카페","hot",2103,4.7,156,
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&q=80",
                "☕ 정가 30,000원 → 18,000원 (40% 할인)\n\n아메리카노 20잔을 한 번에!\n\n✅ 아이스/따뜻한 아메리카노 선택 가능\n✅ 전국 메가커피 직영/가맹점 사용\n✅ 발급일로부터 60일 유효",
                "• 전국 메가커피 직영/가맹점 이용 가능\n• 일부 한정 메뉴 제외", 67, 60),
            build("롯데시네마 관람권 2매","LOTTE CINEMA",28000,18200,35,50,"문화","new",789,4.9,67,
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400&q=80",
                "🎬 정가 28,000원 → 18,200원 (35% 할인)\n\n전국 롯데시네마 영화 관람권 2매 (1인 1매씩 사용)\n\n✅ 일반/프리미엄관 포함\n✅ 주말/공휴일 사용 가능\n✅ 발급일로부터 30일 유효",
                "• 스위트박스, 샤롯데씨어터 제외\n• 1회 2매 동시 사용", 8, 30),
            build("CGV 팝콘L+음료2 콤보","CGV",22000,13200,40,60,"문화","hot",543,4.7,41,
                "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&q=80",
                "🍿 정가 22,000원 → 13,200원 (40% 할인)\n\nCGV 팝콘 L사이즈 1개 + 음료 2잔 콤보\n\n✅ 전국 CGV 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 일부 CGV 특별관 및 씨네드쉐프 제외\n• 음료는 콜라/사이다/아메리카노 중 선택", 34, 30),
            build("bhc 뿌링클 콤보 세트","bhc 치킨",29000,20300,30,200,"음식","hot",3421,4.8,234,
                "https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=400&q=80",
                "🍗 정가 29,000원 → 20,300원 (30% 할인)\n\nbhc 뿌링클 1마리 + 콜라 1.25L + 치즈볼\n\n✅ 전국 bhc 가맹점 사용\n✅ 배달/포장/매장 모두 가능\n✅ 발급일로부터 30일 유효",
                "• 전국 bhc 가맹점 이용 가능\n• 일부 직영점 제외", 149, 30),
            build("맥도날드 빅맥 세트 2인권","MCDONALDS",18000,10800,40,120,"음식","new",1876,4.5,102,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 정가 18,000원 → 10,800원 (40% 할인)\n\n빅맥 세트(버거+감자튀김+음료) 2인 교환권\n\n✅ 전국 맥도날드 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 딜리버리 시 배달비 별도\n• 세트 구성 변경 불가", 45, 30),
            build("GS25 도시락 3종 선택권","GS25",15000,9000,40,300,"음식","hot",4532,4.3,289,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🍱 정가 15,000원 → 9,000원 (40% 할인)\n\nGS25 인기 도시락 3종 중 자유 선택\n\n✅ 전국 GS25 편의점 사용\n✅ 발급일로부터 14일 유효",
                "• 행사 상품 및 한정판 도시락 제외\n• 1인 1매 사용 가능", 182, 14),
            build("올리브영 2만원 상품권","OLIVE YOUNG",20000,14000,30,100,"쇼핑","new",678,4.6,54,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "💄 정가 20,000원 → 14,000원 (30% 할인)\n\n올리브영 오프라인 매장 전용 2만원 상품권\n\n✅ 전국 올리브영 오프라인 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 온라인몰 사용 불가, 오프라인 전용\n• 잔액 환불 불가", 29, 30),
            build("무신사 할인쿠폰 (2만원 할인)","MUSINSA",50000,30000,40,50,"쇼핑","hot",1024,4.7,88,
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&q=80",
                "👗 5만원 이상 구매 시 2만원 할인 쿠폰\n\n✅ 무신사 앱/웹 전용\n✅ 발급일로부터 14일 유효\n✅ 전 상품 적용 (일부 제외)",
                "• 쿠폰 코드 복사 후 무신사 앱에서 등록\n• 5만원 이상 구매 시 적용 (할인 후 금액 기준 제외)\n• 일부 한정 상품 및 특가 상품 제외", 7, 14),
            build("GS칼텍스 주유권 5만원","GS CALTEX",50000,37500,25,40,"생활","end",234,4.4,21,
                "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=400&q=80",
                "⛽ 정가 50,000원 → 37,500원 (25% 할인)\n\nGS칼텍스 주유소 전용 5만원 주유권\n\n✅ 전국 GS칼텍스 주유소 사용\n✅ 발급일로부터 60일 유효",
                "• 주유 외 세차/편의점 사용 불가\n• 잔액 환불 불가", 3, 60),
            build("쿠팡이츠 최대 5천원 할인","COUPANG EATS",10000,5000,50,200,"음식","hot",5621,4.6,412,
                "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&q=80",
                "🛵 1만원 이상 주문 시 최대 5,000원 할인\n\n✅ 쿠팡이츠 앱 전용\n✅ 발급일로부터 7일 유효\n✅ 전 음식점 적용",
                "• 1만원 이상 주문 시 적용\n• 쿠팡이츠 앱에서 결제 시 쿠폰 코드 입력\n• 중복 할인 불가", 149, 7),
            build("신세계백화점 상품권 5만원","SHINSEGAE",50000,35000,30,30,"쇼핑","end",321,4.9,27,
                "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=400&q=80",
                "🏬 정가 50,000원 → 35,000원 (30% 할인)\n\n신세계백화점 전국 오프라인 매장 전용 5만원 상품권\n\n✅ 전국 신세계백화점 사용\n✅ 발급일로부터 60일 유효",
                "• 일부 입점 브랜드 사용 제한\n• 잔액 환불 불가", 5, 60),
            build("쿠팡 로켓배송 1만원 할인","COUPANG",30000,20000,33,300,"쇼핑","hot",8912,4.8,524,
                "https://images.unsplash.com/photo-1457364887197-9150188c107b?w=400&q=80",
                "🚀 3만원 이상 구매 시 1만원 즉시 할인!\n\n✅ 쿠팡 앱 전용 · 로켓배송 상품 적용\n✅ 발급일로부터 7일 유효\n✅ 할인가 20,000원 (정상가 30,000원)",
                "• 3만원 이상 결제 시 자동 적용\n• 로켓배송 상품에 한함\n• 중복 할인 불가", 213, 7),
            build("배달의민족 최대 7,500원 할인","BAEMIN",15000,7500,50,150,"음식","hot",4231,4.7,318,
                "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=400&q=80",
                "🛵 1만5천원 이상 주문 시 최대 7,500원 할인\n\n✅ 배달의민족 앱 전용\n✅ 발급일로부터 7일 유효\n✅ 전 음식점 적용",
                "• 1만5천원 이상 주문 시 적용\n• 배달의민족 앱에서 결제 시 쿠폰 코드 입력\n• 중복 할인 불가", 91, 7)
        );

        productRepo.saveAll(products);
        log.info("🛍️ 핫딜 상품 {}개 생성 완료!", products.size());
    }

    private Product build(String name, String brand, int ori, int dis, int rate, int total,
                          String cat, String badge, int buyers, double rating, int reviewCnt,
                          String img, String desc, String notice, int remaining, int validDays) {
        return Product.builder()
            .productName(name).brandName(brand)
            .originalPrice(ori).discountedPrice(dis).discountRate(rate)
            .totalCount(total).remainingCount(remaining)
            .category(cat).badge(badge).buyers(buyers).rating(rating).reviewCount(reviewCnt)
            .imageUrl(img).description(desc).notice(notice).isActive(true)
            .validDays(validDays)
            .build();
    }
}