package com.github.pblcnr.seraquepaguei.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Será que Paguei? API está funcionando!";
    }
}
