package org.retina.care.backend.application.controllers;

import org.retina.care.backend.core.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @GetMapping("/")
    public ResponseEntity<HttpResponse<String>> getBase() {
       return new ResponseEntity<>(HttpResponse.Ok("API up and running.", null), HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<HttpResponse<String>> getHealth() {
        return new ResponseEntity<>(HttpResponse.Ok("API is healthy.", null), HttpStatus.OK);
    }
}
