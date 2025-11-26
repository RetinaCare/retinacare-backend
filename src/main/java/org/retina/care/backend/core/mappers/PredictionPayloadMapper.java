package org.retina.care.backend.core.mappers;

import org.retina.care.backend.application.dto.prediction.ExternalPredictionPayloadDto;
import org.retina.care.backend.application.dto.prediction.PredictionRequestDto;
import org.springframework.stereotype.Service;

// @Deprecated - this does not get used later
@Service
public class PredictionPayloadMapper {
    public ExternalPredictionPayloadDto toPayload(PredictionRequestDto dto) throws Exception {
        if (dto == null) {
            throw new Exception("PredictionRequestDto is null");
        }
        ExternalPredictionPayloadDto payload = new ExternalPredictionPayloadDto();
        payload.setImage(dto.getImage());
        payload.setHba1c(String.valueOf(dto.getHba1cLevelPercentage()));
        payload.setBloodPressure(String.valueOf(dto.getBloodPressureInMmHg()));
        payload.setDuration(String.valueOf(dto.getDurationInYears()));
        return payload;
    }
}
