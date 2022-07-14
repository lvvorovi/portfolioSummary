package com.portfolioSummary.domain.summaryTotalsDto.service;

import com.portfolioSummary.domain.positionSummaryDto.PositionSummaryDto;
import com.portfolioSummary.domain.summaryTotalsDto.SummaryTotalsDto;

import java.util.List;

public interface SummaryTotalsService {

    SummaryTotalsDto getSummaryTotals(List<PositionSummaryDto> positionSummaryList);

}
