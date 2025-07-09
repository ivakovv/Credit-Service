package com.credit.credit.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Embeddable
public class RequestConditions {
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amount;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal rate;

    @NotNull
    @Min(0)
    private Integer term;

    @Size(min = 1)
    @NotEmpty
    private List<String> featureLabels;
}
