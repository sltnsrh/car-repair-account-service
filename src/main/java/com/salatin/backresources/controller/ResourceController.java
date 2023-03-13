package com.salatin.backresources.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/res")
    public String resource() {
        return "Here your resource";
    }

    @GetMapping("/res2")
    public ResponseEntity<String> resource2() {
        return new ResponseEntity<>("your response", HttpStatus.OK);
    }
}
