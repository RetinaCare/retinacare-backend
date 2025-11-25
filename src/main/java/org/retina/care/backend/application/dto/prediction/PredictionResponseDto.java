package org.retina.care.backend.application.dto.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PredictionResponseDto {
    private Integer predictedClass;
    private Double confidenceScore;
    private String recommendation;
}
