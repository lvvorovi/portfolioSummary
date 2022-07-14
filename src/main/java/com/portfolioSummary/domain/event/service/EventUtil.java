package com.portfolioSummary.domain.event.service;

import com.portfolioSummary.domain.event.eventType.EventType;
import com.portfolioSummary.domain.event.mapper.EventDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolioSummary.domain.event.eventType.EventType.BUY;
import static com.portfolioSummary.domain.event.eventType.EventType.SELL;

public class EventUtil {

    public static BigDecimal getTotalBought(List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(BUY))
                .map(event -> event.getPrice().multiply(event.getShares()))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    public static BigDecimal getTotalSold(List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(event -> event.getPrice().multiply(event.getShares()))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    public static BigDecimal getTotalShares(List<EventDto> eventList) {
        BigDecimal sharesBough = eventList.stream()
                .filter(event -> event.getType().equals(BUY))
                .map(EventDto::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        BigDecimal sharesSold = eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(EventDto::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        return sharesBough.subtract(sharesSold);
    }

    public static BigDecimal getTotalDividends(List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.DIVIDEND))
                .map(EventDto::getPrice)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    public static BigDecimal getTotalCommission(List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> !event.getType().equals(EventType.DIVIDEND))
                .map(EventDto::getCommission)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    public static BigDecimal getCapitalGainCurrencyAdjusted(List<EventDto> eventList,
                                                            BigDecimal currencyRateClientSells,
                                                            BigDecimal currencyRateClientBuys,
                                                            BigDecimal currentSharePrice) {
        List<Share> shareListBought = getSharesBough(
                eventList, currencyRateClientBuys);
        List<Share> shareListSold = getSharesSold(
                eventList, currencyRateClientSells);

        shareListBought.sort(Comparator.comparing(share -> share.date));
        shareListSold.sort(Comparator.comparing(share -> share.date));

        List<Share> liquidatedShares = getLiquidatedShares(shareListBought, shareListSold);

        List<Share> activeShares = new ArrayList<>(shareListBought);
        liquidatedShares.forEach(share -> activeShares.remove(0));

        BigDecimal capitalGainCurrencyAdjustedFromLiquidatedShares =
                getCapitalGainCurrencyAdjustedFromLiquidatedShares(liquidatedShares);

        BigDecimal capitalGainCurrencyAdjustedFromActiveShares = getCapitalGainCurrencyAdjustedFromActiveShares(
                activeShares, currentSharePrice, currencyRateClientSells);

        return capitalGainCurrencyAdjustedFromLiquidatedShares
                .add(capitalGainCurrencyAdjustedFromActiveShares);
    }

    private static BigDecimal getCapitalGainCurrencyAdjustedFromActiveShares(List<Share> activeShares,
                                                                             BigDecimal currentPrice,
                                                                             BigDecimal currencyRateClientSells) {
        return activeShares.stream()
                .map(share -> (currentPrice.subtract(share.priceBought))
                        .divide(currencyRateClientSells, 2, RoundingMode.HALF_DOWN))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private static BigDecimal getCapitalGainCurrencyAdjustedFromLiquidatedShares(List<Share> liquidatedShares) {
        return liquidatedShares.stream()
                .map(share -> (share.priceSold.subtract(share.priceBought))
                        .divide(share.rateSold, 2, RoundingMode.HALF_DOWN))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private static List<Share> getLiquidatedShares(List<Share> shareListBought, List<Share> shareListSold) {
        List<Share> liquidatedShareList = new ArrayList<>();
        for (int i = 0; i < shareListSold.size(); i++) {
            shareListBought.get(i).priceSold = shareListSold.get(i).priceSold;
            shareListBought.get(i).rateSold = shareListSold.get(i).rateSold;
            liquidatedShareList.add(shareListBought.get(i));
        }
        return liquidatedShareList;
    }

    private static List<Share> getSharesSold(List<EventDto> eventList, BigDecimal rateClientSells) {
        return eventList.stream()
                .filter(event -> event.getType().equals(SELL))
                .map(event -> getSharesSold(event, rateClientSells))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<Share> getSharesBough(List<EventDto> eventList, BigDecimal rateClientBuys) {
        return eventList.stream()
                .filter(event -> event.getType().equals(BUY))
                .map(event -> getSharesBough(event, rateClientBuys))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<Share> getSharesSold(EventDto event, BigDecimal rateClientSells) {
        List<Share> shareListSold = new ArrayList<>();
        int transactionSize = event.getShares().intValue();
        for (int i = 0; i < transactionSize; i++) {
            if (event.getType().equals(SELL)) {
                Share share = new Share();
                share.date = event.getDate();
                share.priceSold = event.getPrice();
                share.rateSold = rateClientSells;
                shareListSold.add(share);
            }
        }
        return shareListSold;
    }

    private static List<Share> getSharesBough(EventDto event, BigDecimal rateClientBuys) {
        List<Share> shareListBought = new ArrayList<>();
        int eventSize = event.getShares().intValue();
        for (int i = 0; i < eventSize; i++) {
            if (event.getType().equals(BUY)) {
                Share share = new Share();
                share.date = event.getDate();
                share.priceBought = event.getPrice();
                share.rateBought = rateClientBuys;
                shareListBought.add(share);
            }
        }
        return shareListBought;
    }

}

class Share {
    LocalDate date;
    BigDecimal priceBought;
    BigDecimal priceSold;
    BigDecimal rateBought;
    BigDecimal rateSold;
}


