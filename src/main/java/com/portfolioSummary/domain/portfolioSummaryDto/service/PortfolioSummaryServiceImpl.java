package com.portfolioSummary.domain.portfolioSummaryDto.service;

import com.portfolioSummary.MSUtil.MSUtil;
import com.portfolioSummary.domain.portfolioSummaryDto.PortfolioSummaryDto;
import com.portfolioSummary.domain.position.PositionDto;
import com.portfolioSummary.domain.position.service.PositionService;
import com.portfolioSummary.domain.positionSummaryDto.PositionSummaryDto;
import com.portfolioSummary.domain.positionSummaryDto.service.PositionSummaryService;
import com.portfolioSummary.domain.summaryTotalsDto.SummaryTotalsDto;
import com.portfolioSummary.domain.summaryTotalsDto.service.SummaryTotalsService;
import com.portfolioSummary.payload.PortfolioDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class PortfolioSummaryServiceImpl implements PortfolioSummaryService {

    private final PositionSummaryService positionSummaryService;
    private final PositionService positionService;
    private final SummaryTotalsService summaryTotalsService;
    private final MSUtil msUtil;

    @Override
    public PortfolioSummaryDto getSummaryByPortfolioId(@NumberFormat Long portfolioId) {

        PortfolioDto portfolio = msUtil.findPortfolioById(portfolioId);
        List<PositionDto> positionList = positionService.getPositionList(portfolio);
        List<PositionSummaryDto> positionSummaryList =
                positionSummaryService.getPositionSummaryList(positionList);
        SummaryTotalsDto summaryTotals = summaryTotalsService.getSummaryTotals(positionSummaryList);

        PortfolioSummaryDto portfolioSummary = new PortfolioSummaryDto();

        portfolioSummary.setPortfolioId(portfolio.getId());
        portfolioSummary.setPortfolioName(portfolio.getName());
        portfolioSummary.setPortfolioCurrency(portfolio.getCurrency());
        portfolioSummary.setPortfolioStrategy(portfolio.getStrategy());
        portfolioSummary.setPositionSummaryList(positionSummaryList);
        portfolioSummary.setSummaryTotals(summaryTotals);

        return portfolioSummary;
    }

}
