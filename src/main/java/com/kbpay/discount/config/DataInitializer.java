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

        // 오프라인 상품 설명 템플릿
        // ori = 결제금액 기준, dis = 최대 할인금액, rate = 할인율(%)
        // 최소 결제금액: ori, 최대 할인: dis

        List<Product> products = List.of(
            // ── 카페 (오프라인) ──
            build("스타벅스 바로할인","STARBUCKS",15000,1050,7,150,"카페","hot",8421,4.8,234,
                "https://images.unsplash.com/photo-1541167760496-1628856ab772?w=400&q=80",
                "☕ KB페이로 결제 시 즉시 7% 할인\n\n✅ 15,000원 이상 결제 시 사용 가능\n✅ 최대 1,050원 할인\n✅ 전국 스타벅스 매장",
                "• 타 할인/쿠폰/적립과 중복 적용 불가\n• 인천공항점, 면세점 등 일부 특수 매장 사용 불가\n• 음료·푸드 전 메뉴 적용", 98, 30),
            build("투썸플레이스 바로할인","A TWOSOME PLACE",12000,840,7,100,"카페","new",3241,4.6,98,
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&q=80",
                "🎂 KB페이로 결제 시 즉시 7% 할인\n\n✅ 12,000원 이상 결제 시 사용 가능\n✅ 최대 840원 할인\n✅ 전국 투썸플레이스 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점 등 특수 매장 제외\n• 음료·케이크 전 메뉴 적용", 74, 30),
            build("파리바게뜨 바로할인","PARIS BAGUETTE",10000,600,6,120,"카페","",2187,4.5,76,
                "https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?w=400&q=80",
                "🥐 KB페이로 결제 시 즉시 6% 할인\n\n✅ 10,000원 이상 결제 시 사용 가능\n✅ 최대 600원 할인\n✅ 전국 파리바게뜨 매장",
                "• 타 할인과 중복 적용 불가\n• 고속도로 휴게소·면세점 제외\n• 빵·케이크·음료 전 품목 적용", 67, 30),
            build("배스킨라빈스 바로할인","BASKIN ROBBINS",8000,640,8,120,"카페","",4312,4.7,112,
                "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?w=400&q=80",
                "🍦 KB페이로 결제 시 즉시 8% 할인\n\n✅ 8,000원 이상 결제 시 사용 가능\n✅ 최대 640원 할인\n✅ 전국 배스킨라빈스 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점·면세 구역 제외\n• 아이스크림·케이크 전 메뉴 적용", 78, 30),
            build("탐앤탐스 바로할인","TOM N TOMS",10000,500,5,100,"카페","new",1543,4.5,67,
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&q=80",
                "☕ KB페이로 결제 시 즉시 5% 할인\n\n✅ 10,000원 이상 결제 시 사용 가능\n✅ 최대 500원 할인\n✅ 전국 탐앤탐스 매장",
                "• 타 할인과 중복 적용 불가\n• 일부 특수 매장 제외\n• 음료 전 메뉴 적용", 55, 30),

            // ── 음식 (오프라인) ──
            build("버거킹 바로할인","BURGER KING",15000,1200,8,100,"음식","hot",5621,4.6,156,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 KB페이로 결제 시 즉시 8% 할인\n\n✅ 15,000원 이상 결제 시 사용 가능\n✅ 최대 1,200원 할인\n✅ 전국 버거킹 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점·고속도로 휴게소 제외\n• 세트·단품·사이드 전 메뉴 적용", 67, 30),
            build("피자헛 바로할인","PIZZA HUT",20000,1800,9,80,"음식","hot",3421,4.6,89,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80",
                "🍕 KB페이로 결제 시 즉시 9% 할인\n\n✅ 20,000원 이상 결제 시 사용 가능\n✅ 최대 1,800원 할인\n✅ 전국 피자헛 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점·면세점 제외\n• 피자·파스타·사이드 전 메뉴 적용", 44, 30),
            build("맥도날드 바로할인","MCDONALDS",12000,720,6,150,"음식","",4312,4.5,102,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 KB페이로 결제 시 즉시 6% 할인\n\n✅ 12,000원 이상 결제 시 사용 가능\n✅ 최대 720원 할인\n✅ 전국 맥도날드 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항·고속도로 휴게소점 제외\n• 딜리버리 포함 전 메뉴 적용", 89, 30),
            build("KFC 바로할인","KFC",13000,910,7,80,"음식","",1876,4.5,58,
                "https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=400&q=80",
                "🍗 KB페이로 결제 시 즉시 7% 할인\n\n✅ 13,000원 이상 결제 시 사용 가능\n✅ 최대 910원 할인\n✅ 전국 KFC 매장",
                "• 타 할인과 중복 적용 불가\n• 인천공항점·고속도로 휴게소 제외\n• 버킷·세트·단품 전 메뉴 적용", 42, 30),
            build("도미노피자 바로할인","DOMINOS",20000,2000,10,60,"음식","hot",2341,4.6,95,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80",
                "🍕 KB페이로 결제 시 즉시 10% 할인\n\n✅ 20,000원 이상 결제 시 사용 가능\n✅ 최대 2,000원 할인\n✅ 전국 도미노피자 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점·면세점 제외\n• 피자·사이드·음료 전 메뉴 적용", 38, 30),
            build("서브웨이 바로할인","SUBWAY",8000,480,6,100,"음식","hot",1987,4.4,89,
                "https://images.unsplash.com/photo-1509722747041-616f39b57569?w=400&q=80",
                "🥪 KB페이로 결제 시 즉시 6% 할인\n\n✅ 8,000원 이상 결제 시 사용 가능\n✅ 최대 480원 할인\n✅ 전국 서브웨이 매장",
                "• 타 할인과 중복 적용 불가\n• 인천공항점·고속도로점 제외\n• 샌드위치·음료·쿠키 전 메뉴 적용", 67, 30),
            build("쉐이크쉑 바로할인","SHAKE SHACK",18000,1260,7,50,"음식","end",987,4.8,67,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                "🍔 KB페이로 결제 시 즉시 7% 할인\n\n✅ 18,000원 이상 결제 시 사용 가능\n✅ 최대 1,260원 할인\n✅ 전국 쉐이크쉑 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항점·면세 구역 제외\n• 버거·쉐이크·사이드 전 메뉴 적용", 12, 30),
            build("CU 바로할인","CU",5000,250,5,200,"음식","new",6543,4.2,198,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🏪 KB페이로 결제 시 즉시 5% 할인\n\n✅ 5,000원 이상 결제 시 사용 가능\n✅ 최대 250원 할인\n✅ 전국 CU 편의점",
                "• 타 할인과 중복 적용 불가\n• 담배·교통카드 충전·공과금 제외\n• 식품·음료·생활용품 전 품목 적용", 145, 30),
            build("GS25 바로할인","GS25",5000,250,5,200,"음식","hot",7213,4.3,289,
                "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&q=80",
                "🏪 KB페이로 결제 시 즉시 5% 할인\n\n✅ 5,000원 이상 결제 시 사용 가능\n✅ 최대 250원 할인\n✅ 전국 GS25 편의점",
                "• 타 할인과 중복 적용 불가\n• 담배·교통카드 충전·공과금 제외\n• 식품·음료·생활용품 전 품목 적용", 182, 30),

            // ── 쇼핑 (오프라인) ──
            build("올리브영 바로할인","OLIVE YOUNG",20000,1600,8,100,"쇼핑","new",3421,4.6,54,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "💄 KB페이로 결제 시 즉시 8% 할인\n\n✅ 20,000원 이상 결제 시 사용 가능\n✅ 최대 1,600원 할인\n✅ 전국 올리브영 매장",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항·면세점 제외\n• 뷰티·헬스·생활 전 품목 적용", 56, 30),
            build("이니스프리 바로할인","INNISFREE",15000,1050,7,80,"쇼핑","",2341,4.7,42,
                "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&q=80",
                "🌿 KB페이로 결제 시 즉시 7% 할인\n\n✅ 15,000원 이상 결제 시 사용 가능\n✅ 최대 1,050원 할인\n✅ 전국 이니스프리 매장",
                "• 타 할인과 중복 적용 불가\n• 인천공항·면세점 제외\n• 스킨케어·메이크업 전 품목 적용", 34, 30),
            build("신세계백화점 바로할인","SHINSEGAE",50000,3000,6,50,"쇼핑","end",1543,4.9,27,
                "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=400&q=80",
                "🏬 KB페이로 결제 시 즉시 6% 할인\n\n✅ 50,000원 이상 결제 시 사용 가능\n✅ 최대 3,000원 할인\n✅ 전국 신세계백화점",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 일부 명품 브랜드·면세점 제외\n• 패션·뷰티·식품관 전 품목 적용", 23, 30),
            build("홈플러스 바로할인","HOMEPLUS",20000,1200,6,100,"쇼핑","",2187,4.4,43,
                "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&q=80",
                "🛒 KB페이로 결제 시 즉시 6% 할인\n\n✅ 20,000원 이상 결제 시 사용 가능\n✅ 최대 1,200원 할인\n✅ 전국 홈플러스 매장",
                "• 타 할인과 중복 적용 불가\n• 인천공항점·여행 면세 제외\n• 식품·생활·가전 전 품목 적용", 76, 30),

            // ── 문화 (오프라인) ──
            build("롯데시네마 바로할인","LOTTE CINEMA",10000,700,7,80,"문화","new",3421,4.9,67,
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400&q=80",
                "🎬 KB페이로 결제 시 즉시 7% 할인\n\n✅ 10,000원 이상 결제 시 사용 가능\n✅ 최대 700원 할인\n✅ 전국 롯데시네마",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항·특수관(샤롯데씨어터) 제외\n• 영화 관람권·팝콘 세트 적용", 34, 30),
            build("메가박스 바로할인","MEGABOX",10000,800,8,60,"문화","hot",2187,4.7,41,
                "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&q=80",
                "🍿 KB페이로 결제 시 즉시 8% 할인\n\n✅ 10,000원 이상 결제 시 사용 가능\n✅ 최대 800원 할인\n✅ 전국 메가박스",
                "• 타 할인/쿠폰과 중복 적용 불가\n• 인천공항·특수관 제외\n• 관람권·푸드 콤보 전 메뉴 적용", 28, 30),

            // ── 생활 (오프라인) ──
            build("GS칼텍스 바로할인","GS CALTEX",30000,900,3,60,"생활","",1234,4.4,21,
                "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=400&q=80",
                "⛽ KB페이로 결제 시 즉시 3% 할인\n\n✅ 30,000원 이상 결제 시 사용 가능\n✅ 최대 900원 할인\n✅ 전국 GS칼텍스 주유소",
                "• 타 할인과 중복 적용 불가\n• 고속도로 주유소·일부 셀프 주유소 제외\n• 주유·충전 결제 시 적용 (세차·편의점 제외)", 34, 30),

            // ── 온라인 ──
            build("쿠팡 배달비 무료","COUPANG",3000,3000,100,300,"쇼핑","hot",12432,4.8,524,
                "https://images.unsplash.com/photo-1457364887197-9150188c107b?w=400&q=80",
                "🚀 쿠팡이츠 배달비 무료 쿠폰\n\n✅ 쿠팡이츠 앱 전용\n✅ 15,000원 이상 주문 시 적용\n✅ 발급일로부터 7일 유효",
                "• 쿠팡이츠 앱에서 결제 시 쿠폰 코드 입력\n• 1회 사용 가능 (1인 1매)\n• 중복 할인 불가\n• 일부 특수 음식점 제외", 213, 7),
            build("배민 배달비 무료","BAEMIN",4000,4000,100,200,"음식","hot",8921,4.7,318,
                "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=400&q=80",
                "🛵 배달의민족 배달비 무료 쿠폰\n\n✅ 배달의민족 앱 전용\n✅ 12,000원 이상 주문 시 적용\n✅ 발급일로부터 7일 유효",
                "• 배민 앱에서 결제 시 쿠폰 코드 입력\n• 1회 사용 가능\n• 중복 할인 불가\n• 새벽 배달·B마트 제외", 145, 7),
            build("무신사 배송비 무료","MUSINSA",0,3000,100,100,"쇼핑","end",4231,4.7,88,
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&q=80",
                "👗 무신사 배송비 무료 쿠폰\n\n✅ 무신사 앱/웹 전용\n✅ 금액 제한 없이 사용 가능\n✅ 발급일로부터 14일 유효",
                "• 무신사 앱/웹 결제 시 쿠폰 코드 입력\n• 1회 사용 가능\n• 중복 쿠폰 사용 불가\n• 해외 배송·특수 배송 제외", 43, 14)
        );
        productRepo.saveAll(products);
        log.info("🛍️ 상품 {}개 생성 완료", products.size());
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
