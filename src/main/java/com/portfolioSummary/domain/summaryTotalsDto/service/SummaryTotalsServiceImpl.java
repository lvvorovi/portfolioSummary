package com.portfolioSummary.domain.summaryTotalsDto.service;

import com.portfolioSummary.domain.positionSummaryDto.PositionSummaryDto;
import com.portfolioSummary.domain.summaryTotalsDto.SummaryTotalsDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SummaryTotalsServiceImpl implements SummaryTotalsService {

    @Override
    public SummaryTotalsDto getSummaryTotals(List<PositionSummaryDto> positionSummaryList) {
        SummaryTotalsDto summaryTotals = new SummaryTotalsDto();
        positionSummaryList = positionSummaryList.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .toList();

        summaryTotals.setTotalBought(positionSummaryList.stream()
                .map(PositionSummaryDto::getTotalBough)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setTotalShares(positionSummaryList.stream()
                .map(PositionSummaryDto::getTotalShares)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCurrentValue(positionSummaryList.stream()
                .map(PositionSummaryDto::getCurrentValue)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCapitalGain(positionSummaryList.stream()
                .map(PositionSummaryDto::getCapitalGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setDividend(positionSummaryList.stream()
                .map(PositionSummaryDto::getDividend)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCommission(positionSummaryList.stream()
                .map(PositionSummaryDto::getCommission)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCurrencyGain(positionSummaryList.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .map(PositionSummaryDto::getCurrencyGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setTotalGain(positionSummaryList.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .map(PositionSummaryDto::getTotalGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCapitalReturn(summaryTotals.getCapitalGain()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setDividendReturn(summaryTotals.getDividend()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setCommissionReturn(summaryTotals.getCommission()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setCurrencyReturn(summaryTotals.getCurrencyGain()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setTotalReturn(summaryTotals.getTotalGain()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        return summaryTotals;
    }

}
