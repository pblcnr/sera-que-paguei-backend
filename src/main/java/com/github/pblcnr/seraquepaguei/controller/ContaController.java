package com.github.pblcnr.seraquepaguei.controller;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.service.ContaService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping
    public ResponseEntity<ContaResponseDTO> createConta(@Valid @RequestBody ContaRequestDTO dto, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            ContaResponseDTO createdConta = contaService.createConta(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdConta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> getContasUsuario(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(contaService.getContasUsuario(userId));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<ContaResponseDTO> pagarConta(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            ContaResponseDTO contaPaga = contaService.pagarConta(id, userId);
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
