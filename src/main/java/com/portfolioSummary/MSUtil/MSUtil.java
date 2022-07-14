package com.portfolioSummary.MSUtil;

import com.portfolioSummary.payload.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class MSUtil {

    private final RestTemplate restTemplate;

    public PortfolioDto findPortfolioById(Long portfolioId) {
        String token = MDC.get("token");
        validateTokenNotNull(token);
        PortfolioDto response = restTemplate
                .exchange("http://localhost:8080/api/v1/portfolios/" + portfolioId,
                        HttpMethod.GET, entityRequest(token), PortfolioDto.class)
                .getBody();
        validateResponseNotNull(response);
        return response;
    }

    public List<TransactionDto> findTransactionListByPortfolioId(Long portfolioId) {
        String token = MDC.get("token");
        validateTokenNotNull(token);
        TransactionDto[] response = restTemplate
                .exchange("http://localhost:9000/api/v1/transactions?portfolioId=" + portfolioId,
                        HttpMethod.GET, entityRequest(token), TransactionDto[].class)
                .getBody();
        validateResponseNotNull(response);
        return List.of(response);
    }

    public List<DividendDto> findDividendListByPortfolioId(Long portfolioId) {
        String token = MDC.get("token");
        validateTokenNotNull(token);
        DividendDto[] response = restTemplate
                .exchange("http://localhost:9000/api/v1/dividends?portfolioId=" + portfolioId,
                        HttpMethod.GET, entityRequest(token), DividendDto[].class)
                .getBody();
        validateResponseNotNull(response);
        return List.of(response);
    }

    public String getTickerCurrency(String ticker) {
        String response = restTemplate
                .getForObject("http://localhost:9000/tickers/currency/" + ticker, String.class);
        validateResponseNotNull(response);
        return response;
    }

    public BigDecimal getTickerCurrentPrice(String ticker) {
        BigDecimal response = restTemplate
                .getForObject("http://localhost:9000/tickers/price/actual/" + ticker, BigDecimal.class);
        validateResponseNotNull(response);
        return response;
    }

    public BigDecimal getRateClientSellsForPairOnDate(String portfolioCurrency,
                                                      String tickerCurrency,
                                                      LocalDate onDate) {
        return getCurrencyRateDto(portfolioCurrency, tickerCurrency, onDate).getRateClientSells();
    }

    public BigDecimal getRateClientBuysForPairOnDate(String portfolioCurrency,
                                                     String transactionCurrency,
                                                     LocalDate onDate) {
        return getCurrencyRateDto(portfolioCurrency, transactionCurrency, onDate).getRateClientBuys();
    }

    public List<SplitEventDto> getSplitEventList(String ticker) {
        SplitEventDto[] response = restTemplate
                .getForObject("http://localhost:9000/tickers/events/split/" + ticker, SplitEventDto[].class);
        validateResponseNotNull(response);
        return List.of(response);
    }

    private HttpEntity<Object> entityRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return new HttpEntity<>("body", headers);
    }

    private CurrencyRateDto getCurrencyRateDto(String portfolioCurrency, String tickerCurrency, LocalDate onDate) {
        CurrencyRateDto response = restTemplate
                .getForObject("http://localhost:9000/currency/rate/" +
                        portfolioCurrency + "/" + tickerCurrency + "/" + onDate, CurrencyRateDto.class);
        validateResponseNotNull(response);
        return response;
    }

    private void validateResponseNotNull(Object response) {
        if (response == null) throw new NullResponseMSUtilException("MSUtil received null from yahooMS");
    }

    private void validateTokenNotNull(String token) {
        if (token == null) throw new TokenNullMSUtilException("no token for requestId " + MDC.get("request_id"));
    }
}
