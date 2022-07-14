package com.portfolioSummary.domain.event.mapper;

import com.portfolioSummary.payload.DividendDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventDtoMapperImplTest {

    @Autowired
    EventDtoMapperImpl victim;

    DividendDto dividendDto() {
        DividendDto dividendDto = new DividendDto();
        dividendDto.setAmount(new BigDecimal(100));
        return dividendDto;
    }

    EventDto eventdto() {
        EventDto eventDto = new EventDto();
        eventDto.setPrice(new BigDecimal(200));
        return eventDto;
    }

    @Test
    void dividendToEvent() {
        EventDto result = victim.toEvent(dividendDto());
        assertEquals(eventdto().getPrice(), result.getPrice());
    }

    @Test
    void testToEvent() {
    }

    @Test
    void toDividend() {
    }

    @Test
    void toTransaction() {
    }
}