package com.portfolioSummary.config;

import com.portfolioSummary.domain.event.mapper.EventDto;
import com.portfolioSummary.payload.DividendDto;
import com.portfolioSummary.payload.TransactionDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<TransactionDto, EventDto> transactionToEventTypeMap = mapper
                .createTypeMap(TransactionDto.class, EventDto.class);
        transactionToEventTypeMap.addMapping(TransactionDto::getPrice, EventDto::setPrice);

        TypeMap<DividendDto, EventDto> dividendToEventTypeMap = mapper
                .createTypeMap(DividendDto.class, EventDto.class);
        dividendToEventTypeMap.addMapping(DividendDto::getAmount, EventDto::setPrice);
//        dividendToEventTypeMap.addMapping(dto ->  {
//            BigDecimal amount = dto.getAmount();
//            BigDecimal price = new BigDecimal(2);
//            if (amount == null) return null;
//            return amount.multiply(price);
//
//        }, EventDto::setPrice);


        TypeMap<EventDto, DividendDto> eventToDividendTypeMap = mapper
                .createTypeMap(EventDto.class, DividendDto.class);
        eventToDividendTypeMap.addMapping(EventDto::getPrice, DividendDto::setAmount);

        TypeMap<EventDto, TransactionDto> eventToTransactionTypeMap = mapper
                .createTypeMap(EventDto.class, TransactionDto.class);
        eventToTransactionTypeMap.addMapping(EventDto::getPrice, TransactionDto::setPrice);

        return mapper;
    }

}


