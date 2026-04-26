package com.kbpay.discount.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "TB_PRODUCT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String brandName;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer discountedPrice;
    private Integer totalCount;
    @Version private Long version;
    private Integer remainingCount;
    @Column(length = 500) private String imageUrl;
    @Column(length = 1000) private String description;
    @Column(length = 2000) private String notice;
    private String category;
    private String badge;
    private Integer buyers;
    private Double rating;
    private Integer reviewCount;
    private Boolean isActive;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
        if (this.buyers == null) this.buyers = 0;
        if (this.rating == null) this.rating = 4.5;
        if (this.reviewCount == null) this.reviewCount = 0;
    }

    public boolean hasRemaining() {
        return remainingCount != null && remainingCount > 0;
    }

    public void decreaseRemaining() {
        if (!hasRemaining()) throw new IllegalStateException("선착순 마감되었습니다.");
        this.remainingCount--;
    }
}
