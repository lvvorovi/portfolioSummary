package com.portfolioSummary.domain.positionSummaryDto.service;

import com.portfolioSummary.domain.position.PositionDto;
import com.portfolioSummary.domain.positionSummaryDto.PositionSummaryDto;

import java.util.List;

public interface PositionSummaryService {

    List<PositionSummaryDto> getPositionSummaryList(List<PositionDto> positionList);

}
