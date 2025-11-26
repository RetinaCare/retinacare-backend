package org.retina.care.backend.application.dto.prediction;

import lombok.Data;

@Data
public class ExternalPredictionResponseDto {
    private Double confidence;
    private String recommendation;
    private String risk;
    private Boolean success;
}

