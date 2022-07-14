package com.portfolioSummary.domain.position.service;

import com.portfolioSummary.MSUtil.MSUtil;
import com.portfolioSummary.domain.event.mapper.EventDto;
import com.portfolioSummary.domain.event.service.EventDtoService;
import com.portfolioSummary.domain.position.PositionDto;
import com.portfolioSummary.payload.PortfolioDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.portfolioSummary.domain.event.service.EventUtil.*;

@Service
@Validated
@AllArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final EventDtoService eventDtoService;
    private final MSUtil msUtil;

    @Override
    public List<PositionDto> getPositionList(PortfolioDto portfolio) {
        Map<String, List<EventDto>> eventListMap = eventDtoService.getMapOfTickerAndEventLists(portfolio);
        return getPositionListFromEventListMap(eventListMap, portfolio.getCurrency());
    }

    private List<PositionDto> getPositionListFromEventListMap(Map<String, List<EventDto>> eventsMap,
                                                              String portfolioCurrency) {
        List<PositionDto> positionList = new ArrayList<>();
        eventsMap.forEach((ticker, eventList) -> {
            PositionDto position = new PositionDto();

            String tickerCurrency = msUtil.getTickerCurrency(ticker);
            BigDecimal currencyRateClientSells = msUtil
                    .getRateClientSellsForPairOnDate(portfolioCurrency, tickerCurrency, LocalDate.now());
            BigDecimal currencyRateClientBuys = msUtil
                    .getRateClientBuysForPairOnDate(portfolioCurrency, tickerCurrency, LocalDate.now());

            position.setName(ticker + " converted from " + tickerCurrency + " to " + portfolioCurrency);
            position.setEventList(eventList);
            position.setTotalBought(getTotalBought(eventList));
            position.setTotalSold(getTotalSold(eventList));
            position.setNetOriginalCosts(position.getTotalBought().subtract(position.getTotalSold()));
            position.setTotalShares(getTotalShares(eventList));
            position.setCurrentSharePrice(msUtil.getTickerCurrentPrice(ticker));

            position.setCurrentValue(position.getTotalShares()
                    .multiply(position.getCurrentSharePrice())
                    .divide(currencyRateClientSells, 2, RoundingMode.HALF_DOWN));

            position.setDividend(getTotalDividends(eventList));

            position.setCommission(getTotalCommission(eventList));

            position.setCapitalGain(getCapitalGainCurrencyAdjusted(
                    eventList,
                    currencyRateClientSells,
                    currencyRateClientBuys,
                    position.getCurrentSharePrice()));

            position.setCurrencyGain(position.getCurrentValue()
                    .subtract(position.getNetOriginalCosts())
                    .subtract(position.getCapitalGain()));

            position.setTotalGain(position.getCapitalGain()
                    .add(position.getDividend())
                    .add(position.getCurrencyGain())
                    .subtract(position.getCommission()));

            position.setCurrencyReturn(getAsPercentOfTotalBough(position.getCurrencyGain(), position.getTotalBought()));
            position.setDividendReturn(getAsPercentOfTotalBough(position.getDividend(), position.getTotalBought()));
            position.setCapitalReturn(getAsPercentOfTotalBough(position.getCapitalGain(), position.getTotalBought()));
            position.setCommissionReturn(getAsPercentOfTotalBough(position.getCommission(), position.getTotalBought()));
            position.setTotalReturn(getAsPercentOfTotalBough(position.getTotalGain(), position.getTotalBought()));

            positionList.add(position);
        });
        positionList.sort(Comparator.comparing(position -> position.getEventList().get(0).getDate()));
        return positionList;
    }

    private BigDecimal getAsPercentOfTotalBough(BigDecimal input, BigDecimal totalBough) {
        return input.divide(totalBough, 4, RoundingMode.HALF_DOWN);
    }

}

