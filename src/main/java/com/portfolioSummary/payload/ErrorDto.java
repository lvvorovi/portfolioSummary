package com.portfolioSummary.payload;


import lombok.Data;

@Data
public class ErrorDto {

    private String errorMessage;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
