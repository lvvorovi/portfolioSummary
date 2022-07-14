package com.portfolioSummary.domain.position.service;

import com.portfolioSummary.domain.position.PositionDto;
import com.portfolioSummary.payload.PortfolioDto;

import java.util.List;

public interface PositionService {

    List<PositionDto> getPositionList(PortfolioDto portfolio);
}

