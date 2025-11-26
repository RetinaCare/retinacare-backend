package org.retina.care.backend.application.services;

import org.apache.coyote.BadRequestException;
import org.retina.care.backend.application.dto.prediction.ExternalPredictionResponseDto;
import org.retina.care.backend.application.dto.prediction.PredictionRequestDto;
import org.retina.care.backend.application.dto.prediction.PredictionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;

@Service
public class PredictionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PredictionResponseDto predict(PredictionRequestDto dto) throws Exception{
        final String PREDICTION_SERVICE_API_URL = "http://prediction.209.38.105.246.sslip.io/predict";

        // We need to explicitly build the required multipart request body.
        HttpEntity<MultiValueMap<String, Object>> externalRequest = buildMultipartRequest(dto);

        // Request predictions from external service.
        RestTemplate restTemplate = new RestTemplate();
        ExternalPredictionResponseDto result = restTemplate.postForObject(
                PREDICTION_SERVICE_API_URL,
                externalRequest,
                ExternalPredictionResponseDto.class
        );

        if (result == null) {
            throw new ServiceUnavailableException("Prediction service returned no data");
        }

        logger.info("Prediction was successful: {}", result);

        return new PredictionResponseDto(
                result.getConfidence(),
                result.getRisk(),
                result.getRecommendation()
        );
    }

    private HttpEntity<MultiValueMap<String, Object>> buildMultipartRequest(PredictionRequestDto dto) throws Exception {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        // These values MUST be converted to strings first.
        requestBody.add("hba1c", String.valueOf(dto.getHba1cLevelPercentage()));
        requestBody.add("blood_pressure", String.valueOf(dto.getBloodPressureInMmHg()));
        requestBody.add("duration", String.valueOf(dto.getDurationInYears()));

        // Then we can work with the image.
        byte[] imageBytes = dto.getImage().getBytes();
        if (imageBytes.length == 0) {
            throw new BadRequestException("Image is empty");
        }

        ByteArrayResource imageRes = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return dto.getImage().getOriginalFilename();
            }
        };

        // Image part specific file headers.
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<ByteArrayResource> imagePart = new HttpEntity<>(imageRes, fileHeaders);
        requestBody.add("image", imagePart);

        // The main request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(requestBody, requestHeaders);
    }
}
