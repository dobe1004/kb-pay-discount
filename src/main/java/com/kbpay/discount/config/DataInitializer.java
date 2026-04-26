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
            build("스타벅스 아메리카노 Tall 10잔 이용권","STARBUCKS",65000,39000,40,100,"카페","hot",1243,4.8,89,
                "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=400&q=80",
                "☕ KB페이 바로 할인 단독 특가!\n\n전국 스타벅스 매장 어디서나 사용 가능한 아메리카노 Tall 사이즈 10잔 이용권입니다.\n\n✅ 사용 가능 메뉴: 아이스/따뜻한 아메리카노 Tall\n✅ 사용 기간: 발급일로부터 60일\n✅ 사용 지점: 전국 스타벅스 전 매장",
                "• 모바일 쿠폰 특성상 환불 불가\n• 유효기간 내 미사용 시 자동 소멸"),
            build("투썸플레이스 케이크+음료 세트","A TWOSOME PLACE",32000,19200,40,80,"카페","new",456,4.6,34,
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&q=80",
                "🎂 달콤한 케이크와 음료를 한 번에!\n\n투썸플레이스 인기 케이크(조각) 1개 + 음료 1잔 세트입니다.",
                "• 매장 재고에 따라 케이크 품목 변동 가능"),
            build("메가커피 아이스 아메리카노 20잔","MEGA COFFEE",30000,18000,40,150,"카페","hot",2103,4.7,156,
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&q=80",
                "☕ 가성비 최고! 메가커피 20잔 세트\n\n대용량 아이스 아메리카노 20잔을 저렴하게 즐겨보세요.",
                "• 전국 메가커피 직영/가맹점 이용 가능"),
            build("롯데시네마 영화 관람권 2매","LOTTE CINEMA",28000,18200,35,50,"문화","new",789,4.9,67,
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400&q=80",
                "🎬 주말도 OK! 프리미엄석 포함\n\n전국 롯데시네마에서 영화 2편을 즐길 수 있는 관람권 2매 세트입니다.",
                "• 스위트박스 제외"),
            build("CGV 콤보(팝콘L+음료2) 이용권","CGV",22000,13200,40,60,"문화","hot",543,4.7,41,
                "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&q=80",
                "🍿 영화와 팝콘은 필수!\n\nCGV 팝콘 L사이즈 1개 + 음료 2잔 콤보 이용권입니다.",
                "• 일부 CGV 특별관 제외"),
            build("bhc 뿌링클 콤보 세트","bhc 치킨",29000,20300,30,200,"음식","hot",3421,4.8,234,
                "https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=400&q=80",
                "🍗 인기 1위! 뿌링클 콤보\n\nbhc 시그니처 뿌링클 + 콜라 + 치즈볼!",
                "• 전국 bhc 가맹점 이용 가능"),
            build("맥도날드 빅맥 세트 2인 교환권","McDONALD'S",18000,10800,40,120,"음식","new",1876,4.5,102,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 2인이 함께 즐기는 빅맥 세트!",
                "• 딜리버리 시 배달비 별도"),
            build("GS25 도시락 3종 세트 선택권","GS25",15000,9000,40,300,"음식","hot",4532,4.3,289,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🍱 든든한 한 끼! GS25 도시락",
                "• 행사 상품 및 한정판 도시락 제외"),
            build("올리브영 2만원 상품권","OLIVE YOUNG",20000,14000,30,100,"쇼핑","new",678,4.6,54,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "💄 뷰티 쇼핑의 천국, 올리브영!",
                "• 온라인몰 사용 불가, 오프라인 전용"),
            build("무신사 5만원 할인쿠폰","MUSINSA",50000,30000,40,50,"쇼핑","hot",1024,4.7,88,
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&q=80",
                "👗 무신사 스토어 5만원 즉시 할인!",
                "• 일부 한정 상품 제외"),
            build("GS칼텍스 주유권 5만원","GS CALTEX",50000,37500,25,40,"생활","new",234,4.4,21,
                "https://images.unsplash.com/photo-1545262810-a9b5b5d6dcdf?w=400&q=80",
                "⛽ 기름값 아끼자! GS칼텍스 주유권",
                "• 세차, 편의점 이용 불가"),
            build("쿠팡이츠 1만원 할인쿠폰","COUPANG EATS",10000,5000,50,200,"음식","hot",5621,4.6,412,
                "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&q=80",
                "🛵 배달 음식 1만원 즉시 할인!",
                "• 쿠팡이츠 앱 전용"),
            build("신세계백화점 상품권 5만원","SHINSEGAE",50000,35000,30,30,"쇼핑","end",321,4.9,27,
                "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=400&q=80",
                "🏬 신세계백화점 5만원 상품권",
                "• 일부 입점 브랜드 제한 있음"),
            build("네이버플러스 멤버십 3개월","NAVER",16500,9900,40,100,"생활","new",892,4.5,76,
                "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=400&q=80",
                "🟢 네이버플러스 멤버십 3개월 혜택!",
                "• 기존 멤버십 사용자 이용 불가"),
            build("배달의민족 1.5만원 할인쿠폰","BAEMIN",15000,7500,50,150,"음식","hot",4231,4.7,318,
                "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=400&q=80",
                "🛵 배민에서 1만5천원 즉시 할인!",
                "• 배달의민족 앱 전용")
        );

        productRepo.saveAll(products);
        log.info("🛍️ 핫딜 상품 {}개 생성 완료!", products.size());
    }

    private Product build(String name, String brand, int ori, int dis, int rate, int total,
                          String cat, String badge, int buyers, double rating, int reviewCnt,
                          String img, String desc, String notice) {
        return Product.builder()
            .productName(name).brandName(brand)
            .originalPrice(ori).discountedPrice(dis).discountRate(rate)
            .totalCount(total).remainingCount(total)
            .category(cat).badge(badge).buyers(buyers).rating(rating).reviewCount(reviewCnt)
            .imageUrl(img).description(desc).notice(notice).isActive(true)
            .build();
    }
}
