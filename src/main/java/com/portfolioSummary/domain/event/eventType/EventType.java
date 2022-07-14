package com.portfolioSummary.domain.event.eventType;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public enum EventType {

    BUY("Buy"),
    SELL("Sell"),
    DIVIDEND("Dividend");

    private final String code;

    EventType(@NotEmpty String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
