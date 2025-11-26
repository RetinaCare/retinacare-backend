package org.retina.care.backend.application.dto.prediction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExternalPredictionPayload {
    private MultipartFile image;
    private String hba1c;
    @JsonProperty("blood_pressure")
    private String bloodPressure;
    private String duration;
}