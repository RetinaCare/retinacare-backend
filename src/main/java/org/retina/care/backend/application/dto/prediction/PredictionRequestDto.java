package org.retina.care.backend.application.dto.prediction;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PredictionRequestDto {
    @NotNull(message = "HbA1c Level is required")
    @DecimalMin(value = "2.0", message = "HbA1c level is below the threshold 2.0%")
    @DecimalMax(value = "11.0", message = "HbA1c level is above the threshold 11.0%")
    private Double hba1cLevelPercentage;

    @NotNull(message = "Duration in years is required")
    @Min(value = 1, message = "Duration (in years) is below the threshold 1 year")
    @Max(value = 100, message = "Duration (in years) is above the threshold 100 years")
    private Integer durationInYears;

    @NotNull(message = "Blood Pressure (mmHg) is required")
    @Min(value = 100, message = "Blood pressure (mmHg) is below the threshold 100 mmHg")
    @Max(value = 180, message = "Blood pressure (mmHg) is above the threshold 180 mmHg")
    private Integer bloodPressureInMmHg;
}
