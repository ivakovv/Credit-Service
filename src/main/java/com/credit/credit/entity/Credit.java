package com.credit.credit.entity;

import com.credit.credit.enums.CreditStatus;
import com.credit.credit.enums.CreditType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CreditType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CreditStatus status;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "term")
    private Integer term;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
