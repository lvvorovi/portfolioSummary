package com.portfolioSummary.domain.event.mapper;

import com.portfolioSummary.payload.DividendDto;
import com.portfolioSummary.payload.TransactionDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventDtoMapperImpl implements EventDtoMapper {

    private final ModelMapper mapper;

    @Override
    public EventDto toEvent(TransactionDto transaction) {
        return mapper.map(transaction, EventDto.class);
    }

    @Override
    public EventDto toEvent(DividendDto dividend) {
        return mapper.map(dividend, EventDto.class);
    }

    @Override
    public DividendDto toDividend(EventDto event) {
        return mapper.map(event, DividendDto.class);
    }

    @Override
    public TransactionDto toTransaction(EventDto event) {
        return mapper.map(event, TransactionDto.class);
    }

}
