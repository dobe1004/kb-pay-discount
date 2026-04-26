package com.kbpay.discount.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "TB_WISH",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wish {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false) private String userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") private Product product;
    private LocalDateTime createdAt;
    @PrePersist public void prePersist() { this.createdAt = LocalDateTime.now(); }
}
