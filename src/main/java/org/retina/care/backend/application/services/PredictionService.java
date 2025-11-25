package org.retina.care.backend.application.services;

import org.retina.care.backend.application.dto.prediction.PredictionRequestDto;
import org.retina.care.backend.application.dto.prediction.PredictionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PredictionResponseDto predict(PredictionRequestDto dto) throws Exception{
        logger.debug("Prediction request: {}", dto);
        return new PredictionResponseDto(
                1,
                0.5,
                "No action needed"
        );
    }
}
