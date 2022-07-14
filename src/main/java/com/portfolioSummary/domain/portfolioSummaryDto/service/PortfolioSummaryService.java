package com.portfolioSummary.domain.portfolioSummaryDto.service;

import com.portfolioSummary.domain.portfolioSummaryDto.PortfolioSummaryDto;

public interface PortfolioSummaryService {
    PortfolioSummaryDto getSummaryByPortfolioId(Long portfolioId);

}
