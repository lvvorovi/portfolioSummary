package com.portfolioSummary.controller;

import com.portfolioSummary.domain.portfolioSummaryDto.PortfolioSummaryDto;
import com.portfolioSummary.domain.portfolioSummaryDto.service.PortfolioSummaryService;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PortfolioSummaryController {

    private final PortfolioSummaryService service;

    @GetMapping("/portfolio/summary/{id}")
    public ResponseEntity<PortfolioSummaryDto> findById(@PathVariable Long id,
                                                        @RequestHeader("Authorization") String token) {
        try {
            MDC.put("token", token);
            return ResponseEntity.ok(service.getSummaryByPortfolioId(id));
        } finally {
            MDC.clear();
        }
    }
}
