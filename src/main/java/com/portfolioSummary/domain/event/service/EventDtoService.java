package com.portfolioSummary.domain.event.service;

import com.portfolioSummary.domain.event.mapper.EventDto;
import com.portfolioSummary.payload.PortfolioDto;

import java.util.List;
import java.util.Map;

public interface EventDtoService {

    Map<String, List<EventDto>> getMapOfTickerAndEventLists(PortfolioDto portfolio);

}
