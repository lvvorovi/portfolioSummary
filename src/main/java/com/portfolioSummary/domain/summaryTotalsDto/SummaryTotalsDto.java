package com.portfolioSummary.domain.summaryTotalsDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Validated
@Data
public class SummaryTotalsDto {

    @NotNull
    @NumberFormat
    private BigDecimal totalBought;
    @NotNull
    @PositiveOrZero
    private BigDecimal totalShares;
    @NotNull
    @NumberFormat
    @PositiveOrZero
    private BigDecimal currentValue;
    @NotNull
    @NumberFormat
    private BigDecimal capitalGain;
    @NotNull
    @NumberFormat
    @PositiveOrZero
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
    @JsonProperty("capitalReturn %")
    private BigDecimal capitalReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("commissionReturn %")
    private BigDecimal commissionReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("currencyReturn %")
    private BigDecimal currencyReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("dividendReturn %")
    private BigDecimal DividendReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("totalReturn %")
    private BigDecimal totalReturn;

}
