package com.kbpay.discount.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "TB_USER")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @Column(name = "USER_ID", length = 50)
    private String userId;
    @Column(nullable = false) private String password;
    private String grade;           // 일반, 실버, 골드, VIP
    private Integer pointBalance;
    private Boolean isAdmin;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.grade == null) this.grade = "일반";
        if (this.pointBalance == null) this.pointBalance = 0;
        if (this.isAdmin == null) this.isAdmin = false;
    }
}
