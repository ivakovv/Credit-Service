package com.credit.credit.entity;

import com.credit.credit.enums.CreditStatus;
import com.credit.credit.enums.CreditType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "credits")
public class Credit {
    /**
     * Id кредита внутри системы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Id бизнес транзакции
     * Генерируется автоматически
     */
    @Column(columnDefinition = "UUID", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID businessTransactionId;

    /**
     * Версия
     */
    @Column(name = "version", nullable = false)
    @Min(1)
    private Integer version;

    /**
     * Тип кредита
     * @see CreditType
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private CreditType type;


    /**
     * Статус кредита
     * @see CreditStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private CreditStatus status;

    /**
     * Параметры кредита
     * @see RequestConditions
     */
    @Embedded
    @Valid
    @NotNull
    private RequestConditions requestConditions;

    /**
     * Дата создания кредита
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    /**
     * Дата обновления кредита
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @PrePersist
    protected void generateBusinessTransactionId() {
        if (this.businessTransactionId == null) {
            this.businessTransactionId = UUID.randomUUID();
        }
    }

}
