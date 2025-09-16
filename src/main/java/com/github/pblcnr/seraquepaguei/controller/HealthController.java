package com.github.pblcnr.seraquepaguei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "Status da aplicação")
@RestController
public class HealthController {

    @Operation(summary = "Verificar status", description = "Endpoint público para verificar se API está online")
    @GetMapping("/health")
    public String health() {
        return "Será que Paguei? API está funcionando!";
    }
}
