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
                "☕ 정가 65,000원 → 39,000원 (40% 할인)\n\n아메리카노 Tall 사이즈 10잔\n\n✅ 아이스/핫 선택 가능\n✅ 전국 스타벅스 직영점 사용\n✅ 발급일로부터 60일 유효",
                "• 스타벅스 직영점에서만 사용 가능\n• 텀블러 할인과 중복 적용 불가", 67, 60),
            build("투썸플레이스 케이크+음료 세트","A TWOSOME PLACE",32000,19200,40,80,"카페","new",456,4.6,34,
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&q=80",
                "🎂 정가 32,000원 → 19,200원 (40% 할인)\n\n홀케이크 1호 + 아이스 음료 2잔 세트\n\n✅ 전국 투썸플레이스 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 시즌 케이크 및 한정 케이크 제외\n• 음료 사이즈 변경 불가", 44, 30),
            build("무신사 할인쿠폰 (2만원 할인)","MUSINSA",50000,30000,40,50,"쇼핑","end",1024,4.7,88,
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&q=80",
                "👗 5만원 이상 구매 시 2만원 할인\n\n✅ 무신사 앱/웹 전용\n✅ 발급일로부터 14일 유효",
                "• 쿠폰 코드 복사 후 무신사 앱에서 등록\n• 5만원 이상 구매 시 적용\n• 일부 한정 상품 및 특가 상품 제외", 7, 14),
            build("롯데시네마 관람권 2매","LOTTE CINEMA",28000,18200,35,50,"문화","end",789,4.9,67,
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400&q=80",
                "🎬 정가 28,000원 → 18,200원 (35% 할인)\n\n전국 롯데시네마 영화 관람권 2매\n\n✅ 일반/프리미엄관 포함\n✅ 주말/공휴일 사용 가능\n✅ 발급일로부터 30일 유효",
                "• 스위트박스, 샤롯데씨어터 제외\n• 1회 2매 동시 사용", 8, 30),
            build("메가박스 팝콘L+음료2 콤보","MEGABOX",20000,12000,40,60,"문화","new",543,4.7,41,
                "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&q=80",
                "🍿 정가 20,000원 → 12,000원 (40% 할인)\n\n메가박스 팝콘 L사이즈 1개 + 음료 2잔 콤보\n\n✅ 전국 메가박스 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 일부 특별관 제외\n• 음료는 콜라/사이다/아메리카노 중 선택", 34, 30),
            build("피자헛 라지 피자 1판","PIZZA HUT",28000,18200,35,80,"음식","hot",3421,4.6,234,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80",
                "🍕 정가 28,000원 → 18,200원 (35% 할인)\n\n피자헛 라지 사이즈 1판 교환권\n\n✅ 전국 피자헛 매장 사용\n✅ 배달/포장/매장 모두 가능\n✅ 발급일로부터 30일 유효",
                "• 프리미엄 메뉴 일부 제외\n• 시즌 한정 메뉴 추가 금액 발생 가능", 149, 30),
            build("맥도날드 빅맥 세트 2인권","MCDONALDS",18000,10800,40,120,"음식","new",1876,4.5,102,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 정가 18,000원 → 10,800원 (40% 할인)\n\n빅맥 세트 2인 교환권\n\n✅ 전국 맥도날드 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 딜리버리 시 배달비 별도\n• 세트 구성 변경 불가", 45, 30),
            build("GS25 도시락 3종 선택권","GS25",15000,9000,40,300,"음식","hot",4532,4.3,289,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🍱 정가 15,000원 → 9,000원 (40% 할인)\n\nGS25 인기 도시락 3종 중 자유 선택\n\n✅ 전국 GS25 편의점 사용\n✅ 발급일로부터 14일 유효",
                "• 행사 상품 및 한정판 도시락 제외\n• 1인 1매 사용 가능", 182, 14),
            build("올리브영 2만원 상품권","OLIVE YOUNG",20000,14000,30,100,"쇼핑","new",678,4.6,54,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "💄 정가 20,000원 → 14,000원 (30% 할인)\n\n올리브영 오프라인 매장 전용 2만원 상품권\n\n✅ 전국 올리브영 오프라인 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 온라인몰 사용 불가, 오프라인 전용\n• 잔액 환불 불가", 29, 30),
            build("버거킹 와퍼 세트 2인권","BURGER KING",18000,11700,35,100,"음식","hot",2103,4.6,156,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 정가 18,000원 → 11,700원 (35% 할인)\n\n버거킹 와퍼 세트 2인 교환권\n\n✅ 전국 버거킹 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 매장/포장/딜리버리 모두 가능\n• 세트 구성 변경 불가", 67, 30),
            build("GS칼텍스 주유권 5만원","GS CALTEX",50000,37500,25,40,"생활","end",234,4.4,21,
                "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=400&q=80",
                "⛽ 정가 50,000원 → 37,500원 (25% 할인)\n\nGS칼텍스 주유소 전용 5만원 주유권\n\n✅ 전국 GS칼텍스 주유소 사용\n✅ 발급일로부터 60일 유효",
                "• 주유 외 세차/편의점 사용 불가\n• 잔액 환불 불가", 3, 60),
            build("신세계백화점 상품권 5만원","SHINSEGAE",50000,35000,30,30,"쇼핑","end",321,4.9,27,
                "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=400&q=80",
                "🏬 정가 50,000원 → 35,000원 (30% 할인)\n\n신세계백화점 전국 오프라인 매장 전용 5만원 상품권\n\n✅ 전국 신세계백화점 사용\n✅ 발급일로부터 60일 유효",
                "• 일부 입점 브랜드 사용 제한\n• 잔액 환불 불가", 5, 60),
            build("쿠팡 로켓배송 1만원 할인","COUPANG",30000,20000,33,300,"쇼핑","hot",8912,4.8,524,
                "https://images.unsplash.com/photo-1457364887197-9150188c107b?w=400&q=80",
                "🚀 3만원 이상 구매 시 1만원 즉시 할인!\n\n✅ 쿠팡 앱 전용 · 로켓배송 상품 적용\n✅ 발급일로부터 7일 유효",
                "• 3만원 이상 결제 시 자동 적용\n• 로켓배송 상품에 한함\n• 중복 할인 불가", 213, 7),
            build("배달의민족 최대 7,500원 할인","BAEMIN",15000,7500,50,150,"음식","hot",4231,4.7,318,
                "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=400&q=80",
                "🛵 1만5천원 이상 주문 시 최대 7,500원 할인\n\n✅ 배달의민족 앱 전용\n✅ 발급일로부터 7일 유효",
                "• 1만5천원 이상 주문 시 적용\n• 배달의민족 앱에서 결제 시 쿠폰 코드 입력\n• 중복 할인 불가", 91, 7),
            build("파리바게뜨 케이크 할인","PARIS BAGUETTE",25000,17500,30,80,"카페","new",1234,4.5,76,
                "https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?w=400&q=80",
                "🎂 정가 25,000원 → 17,500원 (30% 할인)\n\n파리바게뜨 케이크 1개 할인쿠폰\n\n✅ 전국 파리바게뜨 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 일부 시즌 케이크 제외\n• 1인 1매 사용", 44, 30),
            build("서브웨이 풋롱 2개 세트","SUBWAY",22000,14300,35,100,"음식","hot",987,4.4,89,
                "https://images.unsplash.com/photo-1509722747041-616f39b57569?w=400&q=80",
                "🥪 정가 22,000원 → 14,300원 (35% 할인)\n\n서브웨이 풋롱(30cm) 2개 교환권\n\n✅ 전국 서브웨이 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 매장/포장 전용\n• 소스/채소 자유 선택 가능", 67, 30),
            build("홈플러스 상품권 1만원","HOMEPLUS",10000,7000,30,150,"쇼핑","new",654,4.4,43,
                "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&q=80",
                "🛒 정가 10,000원 → 7,000원 (30% 할인)\n\n홈플러스 전국 매장 전용 1만원 상품권\n\n✅ 전국 홈플러스 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 온라인몰 사용 불가\n• 잔액 환불 불가", 98, 30),
            build("탐앤탐스 아메리카노 10잔","TOM N TOMS",20000,13000,35,120,"카페","new",1876,4.6,134,
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&q=80",
                "☕ 정가 20,000원 → 13,000원 (35% 할인)\n\n탐앤탐스 아메리카노 10잔 이용권\n\n✅ 아이스/따뜻한 선택 가능\n✅ 전국 탐앤탐스 매장 사용\n✅ 발급일로부터 60일 유효",
                "• 전국 탐앤탐스 매장 이용 가능\n• 일부 메뉴 추가 금액 발생", 73, 60),
            build("KFC 버켓 세트 할인","KFC",28000,18200,35,60,"음식","hot",765,4.5,58,
                "https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=400&q=80",
                "🍗 정가 28,000원 → 18,200원 (35% 할인)\n\nKFC 오리지널 버켓 세트 교환권\n\n✅ 전국 KFC 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 매장/포장 전용\n• 딜리버리 시 추가 배달비 발생", 31, 30),
            build("CU 편의점 상품권 5천원","CU",5000,3500,30,200,"음식","new",2341,4.2,198,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🏪 정가 5,000원 → 3,500원 (30% 할인)\n\nCU 편의점 전용 5천원 상품권\n\n✅ 전국 CU 편의점 사용\n✅ 발급일로부터 14일 유효",
                "• 일부 상품 제외\n• 잔액 환불 불가", 127, 14),
            build("쉐이크쉑 버거 세트 2인","SHAKE SHACK",22000,14300,35,60,"음식","end",432,4.8,67,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 정가 22,000원 → 14,300원 (35% 할인)\n\n쉐이크쉑 스택버거 세트 2인 교환권\n\n✅ 전국 쉐이크쉑 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 매장/포장 전용\n• 메뉴 변경 불가", 8, 30),
            build("도미노피자 라지 피자 2판","DOMINOS",44000,28600,35,50,"음식","hot",1123,4.6,95,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80",
                "🍕 정가 44,000원 → 28,600원 (35% 할인)\n\n도미노피자 라지 사이즈 2판 교환권\n\n✅ 전국 도미노피자 매장\n✅ 배달/포장/매장 모두 가능\n✅ 발급일로부터 30일 유효",
                "• 프리미엄 피자 일부 제외\n• 사이드 메뉴 별도", 29, 30),
            build("이니스프리 3만원 할인쿠폰","INNISFREE",30000,21000,30,40,"쇼핑","new",567,4.7,42,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "🌿 3만원 이상 구매 시 9,000원 할인\n\n이니스프리 오프라인/온라인 매장 전용\n\n✅ 전국 이니스프리 매장 사용\n✅ 발급일로부터 14일 유효",
                "• 세일 상품과 중복 사용 불가\n• 3만원 이상 구매 시 적용", 22, 14),
            build("배스킨라빈스 패밀리 세트","BASKIN ROBBINS",24000,16800,30,90,"카페","hot",1432,4.7,112,
                "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?w=400&q=80",
                "🍦 정가 24,000원 → 16,800원 (30% 할인)\n\n배스킨라빈스 패밀리 사이즈 세트\n\n✅ 전국 배스킨라빈스 매장 사용\n✅ 발급일로부터 30일 유효",
                "• 한정 신제품 제외\n• 포장 전용", 56, 30)
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
