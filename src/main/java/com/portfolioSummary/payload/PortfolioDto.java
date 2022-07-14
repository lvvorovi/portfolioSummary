package com.portfolioSummary.payload;

import com.portfolioSummary.core.validation.annotation.Currency;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Data
public class PortfolioDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String strategy;
    @Currency
    private String currency;
    @NotBlank
    private String username;

}
