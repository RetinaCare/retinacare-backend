package org.retina.care.backend.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.retina.care.backend.core.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Base")
public class BaseController {
    @GetMapping("/")
    @Operation(summary = "Ping server status. An OK response indicates that the server is online.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API up and running."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<HttpResponse<String>> getBase() {
       return new ResponseEntity<>(HttpResponse.Ok("API up and running.", null), HttpStatus.OK);
    }

    @GetMapping("/health")
    @Operation(summary = "Ping other services. An OK response indicates services are healthy.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Services are healthy."),
            @ApiResponse(responseCode = "503", description = "Service unavailable.")
    })
    public ResponseEntity<HttpResponse<String>> getHealth() {
        return new ResponseEntity<>(HttpResponse.Ok("Services are healthy.", null), HttpStatus.OK);
    }
}
