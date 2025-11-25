package org.retina.care.backend.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.retina.care.backend.application.dto.prediction.PredictionRequestDto;
import org.retina.care.backend.application.dto.prediction.PredictionResponseDto;
import org.retina.care.backend.application.services.PredictionService;
import org.retina.care.backend.core.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("predict")
@Tag(name = "Prediction")
public class PredictionController {
    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping()
    @Operation(summary = "Access diabetic retinopathy progression risk.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request successful."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Client error with request.",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class))
            )
    })
    public ResponseEntity<HttpResponse<PredictionResponseDto>> predict(@Valid @RequestBody PredictionRequestDto dto) {
        try {
            PredictionResponseDto payload = this.predictionService.predict(dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(HttpResponse.Ok("Prediction successful", payload));
        } catch (Exception e) {
            // A bit of duplication in that I have to manually check this for every method,
            // I should find a way to move it to a middleware or something.
            if (e instanceof BadRequestException) {
               return ResponseEntity
                       .status(HttpStatus.BAD_REQUEST)
                       .body(HttpResponse.BadRequest("Bad Request", e.getMessage()));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.InternalError());
        }
    }
}
