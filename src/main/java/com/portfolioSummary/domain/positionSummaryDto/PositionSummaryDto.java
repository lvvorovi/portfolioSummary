package com.portfolioSummary.domain.positionSummaryDto;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Data
public class PositionSummaryDto {

    @NotBlank
    private String ticker;
    @NotNull
    @NumberFormat
    private BigDecimal totalBough;
    @NotNull
    @NumberFormat
    private BigDecimal totalShares;
    @NotNull
    @NumberFormat
    private BigDecimal currentSharePrice;
    @NotNull
    @NumberFormat
    private BigDecimal currentValue;
    @NotNull
    @NumberFormat
    private BigDecimal capitalGain;
    @NotNull
    @NumberFormat
    private BigDecimal dividend;
    @NotNull
    @NumberFormat
    private BigDecimal commission;
    @NotNull
    @NumberFormat
    private BigDecimal currencyGain;
    @NotNull
    @NumberFormat
    private BigDecimal totalGain;
    @NotNull
    @NumberFormat
    private BigDecimal totalReturn;

}
