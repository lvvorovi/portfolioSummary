package com.portfolioSummary.payload;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
public class SplitEventDto {

    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal amount;
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotBlank
    private String type;
    @NotBlank
    private String data;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal numerator;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal denominator;
    @NotEmpty
    private String splitRatio;

}
