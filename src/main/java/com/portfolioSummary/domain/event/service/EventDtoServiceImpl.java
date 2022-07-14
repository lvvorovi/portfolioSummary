package com.portfolioSummary.domain.event.service;

import com.portfolioSummary.MSUtil.MSUtil;
import com.portfolioSummary.domain.event.eventType.EventType;
import com.portfolioSummary.domain.event.mapper.EventDto;
import com.portfolioSummary.domain.event.mapper.EventDtoMapper;
import com.portfolioSummary.payload.DividendDto;
import com.portfolioSummary.payload.PortfolioDto;
import com.portfolioSummary.payload.SplitEventDto;
import com.portfolioSummary.payload.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventDtoServiceImpl implements EventDtoService {

    private final EventDtoMapper eventMapper;
    private final MSUtil msUtil;

    @Override
    public Map<String, List<EventDto>> getMapOfTickerAndEventLists(PortfolioDto portfolio) {
        List<EventDto> eventList = new ArrayList<>();

        List<TransactionDto> transactionList = msUtil.findTransactionListByPortfolioId(portfolio.getId());
        Map<String, List<SplitEventDto>> splitEventMap = getSplitEventMap(transactionList);
        transactionList = adjustToSplits(transactionList, splitEventMap);
        transactionList = adjustTransactionListToCurrencyRate(transactionList, portfolio.getCurrency());

        List<DividendDto> dividendList = msUtil.findDividendListByPortfolioId(portfolio.getId());
        adjustDividendListToCurrencyRate(dividendList, portfolio.getCurrency());

        eventList.addAll(transactionList.stream()
                .map(eventMapper::toEvent)
                .toList());

        eventList.addAll(dividendList.stream()
                .map(eventMapper::toEvent)
                .toList());

        Map<String, List<EventDto>> eventMap = sortEventListToMapByTickers(eventList);
        eventMap.forEach((ticker, listOfEvents) -> listOfEvents.sort(Comparator.comparing(EventDto::getDate)));
        return eventMap;
    }

    private Map<String, List<EventDto>> sortEventListToMapByTickers(List<EventDto> eventList) {
        Map<String, List<EventDto>> eventMapByTicker = new TreeMap<>();

        eventList.forEach(event -> {
            if (eventMapByTicker.containsKey(event.getTicker())) {
                eventMapByTicker.get(event.getTicker()).add(event);
            } else {
                List<EventDto> newEventListDto = new ArrayList<>();
                newEventListDto.add(event);
                eventMapByTicker.put(event.getTicker(), newEventListDto);
            }
        });

        return eventMapByTicker;
    }

    private List<TransactionDto> adjustTransactionListToCurrencyRate(List<TransactionDto> transactionList,
                                                                     String portfolioCurrency) {
        transactionList = transactionList.stream().peek(transaction -> {
                    String transactionCurrency = msUtil.getTickerCurrency(transaction.getTicker());

                    if (!transactionCurrency.equalsIgnoreCase(portfolioCurrency)) {
                        BigDecimal currencyRateClientBuys = msUtil.getRateClientBuysForPairOnDate(
                                portfolioCurrency, transactionCurrency, transaction.getDate());
                        BigDecimal currencyRateClientSells = msUtil.getRateClientSellsForPairOnDate(
                                portfolioCurrency, transactionCurrency, transaction.getDate());

                        if (transaction.getType().equals(EventType.BUY)) {
                            transaction.setBought(
                                    adjustToCurrencyRate(transaction.getBought(), currencyRateClientBuys));
                            transaction.setCommission(
                                    adjustToCurrencyRate(transaction.getCommission(), currencyRateClientBuys));
                        }

                        if (transaction.getType().equals(EventType.SELL)) {
                            transaction.setSold(
                                    adjustToCurrencyRate(transaction.getSold(), currencyRateClientSells));
                            transaction.setCommission(
                                    adjustToCurrencyRate(transaction.getCommission(), currencyRateClientSells));
                        }
                    }
                })
                .collect(Collectors.toList());
        return transactionList;
    }

    private void adjustDividendListToCurrencyRate(List<DividendDto> dividendList, String portfolioCurrency) {
        dividendList.forEach(dividend -> {
            String dividendCurrency = msUtil.getTickerCurrency(dividend.getTicker());
            if (!dividendCurrency.equalsIgnoreCase(portfolioCurrency)) {
                BigDecimal dividendCurrencyRate = msUtil
                        .getRateClientSellsForPairOnDate(portfolioCurrency, dividendCurrency, dividend.getDate());
                dividend.setAmount(adjustToCurrencyRate(dividend.getAmount(), dividendCurrencyRate));
            }
        });
    }

    private BigDecimal adjustToCurrencyRate(BigDecimal input, BigDecimal currencyRate) {
        return input.divide(currencyRate, 2, RoundingMode.HALF_DOWN);
    }

    private Map<String, List<SplitEventDto>> getSplitEventMap(List<TransactionDto> transactionList) {
        Map<String, List<SplitEventDto>> splitEventMap = new HashMap<>();
        transactionList.forEach(transaction ->
                splitEventMap.put(
                        transaction.getTicker(),
                        msUtil.getSplitEventList(transaction.getTicker())));
        return splitEventMap;
    }

    private List<TransactionDto> adjustToSplits(List<TransactionDto> transactionList,
                                                Map<String, List<SplitEventDto>> splitEventMap) {
        return transactionList.stream()
                .peek(transaction -> splitEventMap.forEach((ticker, eventList) -> {
                    if (transaction.getTicker().equals(ticker)) {
                        eventList.forEach(event -> {
                            if (transaction.getDate().isBefore(event.getDate())) {
                                transaction.setShares(transaction.getShares().multiply(event.getNumerator())
                                        .divide(event.getDenominator(), 0, RoundingMode.HALF_UP));
                                transaction.setPrice(transaction.getPrice().multiply(event.getDenominator())
                                        .divide(event.getNumerator(), 2, RoundingMode.HALF_UP));
                            }
                        });
                    }
                }))
                .collect(Collectors.toList());
    }

}
