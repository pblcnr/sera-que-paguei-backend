package com.github.pblcnr.seraquepaguei.controller;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ContaResponseDTO> createConta(@Valid @RequestBody ContaRequestDTO dto, @PathVariable Long userId) {
        try {
            ContaResponseDTO createdConta = contaService.createConta(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdConta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContaResponseDTO>> getContasUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(contaService.getContasUsuario(userId));
    }

    @PutMapping("/{contaId}/pagar")
    public ResponseEntity<ContaResponseDTO> pagarConta(@PathVariable Long contaId) {
        try {
            ContaResponseDTO contaPaga = contaService.pagarConta(contaId);
            return ResponseEntity.ok(contaPaga);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vencendo-hoje")
    public ResponseEntity<List<ContaResponseDTO>> contasVencendoHoje() {
        return ResponseEntity.ok(contaService.contasVencendoHoje());
    }
}
