package com.portfolioSummary.domain.position;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioSummary.domain.event.mapper.EventDto;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@Validated
@Data
public class PositionDto {

    @NotBlank
    private String name;
    @NotNull
    private List<EventDto> eventList;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal netOriginalCosts;
    @PositiveOrZero
    private BigDecimal totalShares;
    private BigDecimal currentSharePrice;
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
    private BigDecimal currencyGain;
    @NotNull
    @NumberFormat
    private BigDecimal commission;
    @NotNull
    @NumberFormat
    private BigDecimal totalGain;
    @NotNull
    @NumberFormat
    @JsonProperty("capitalReturn_%")
    private BigDecimal capitalReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("dividendReturn_%")
    private BigDecimal dividendReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("currencyReturn_%")
    private BigDecimal currencyReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("commissionReturn_%")
    private BigDecimal commissionReturn;
    @NotNull
    @NumberFormat
    @JsonProperty("totalReturn_%")
    private BigDecimal totalReturn;
    @NotNull
    @NumberFormat
    @JsonIgnore
    private BigDecimal totalBought;
    @NotNull
    @NumberFormat
    @JsonIgnore
    private BigDecimal totalSold;

}
