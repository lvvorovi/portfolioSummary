package com.portfolioSummary.domain.event.mapper;


import com.portfolioSummary.payload.DividendDto;
import com.portfolioSummary.payload.TransactionDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface EventDtoMapper {

    @Valid EventDto toEvent(TransactionDto transaction);

    @Valid EventDto toEvent(DividendDto dividend);

    @Valid DividendDto toDividend(EventDto event);

    @Valid TransactionDto toTransaction(EventDto event);
}
