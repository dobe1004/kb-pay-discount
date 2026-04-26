package com.kbpay.discount.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "TB_TRANSACTION")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") private Product product;

    private String couponCode;
    private Integer paidAmount;
    private Integer savedAmount;
    private Integer originalAmount;
    private Integer pointUsed;
    private Integer pointEarned;

    @Enumerated(EnumType.STRING)
    private TxStatus status;

    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    @PrePersist
    public void prePersist() {
        this.paidAt = LocalDateTime.now();
        if (this.status == null) this.status = TxStatus.PAID;
    }

    public enum TxStatus { PAID, CANCELLED }
}
