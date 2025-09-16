package com.github.pblcnr.seraquepaguei.controller;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;
import com.github.pblcnr.seraquepaguei.scheduler.NotificationScheduler;
import com.github.pblcnr.seraquepaguei.service.ContaService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Contas", description = "Gerenciamento de contas")
@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;
    @Autowired
    private NotificationScheduler notificationScheduler;

    @Operation(summary = "Criar nova conta", description = "Cria uma nova conta para o usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Data de vencimento inválida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PostMapping
    public ResponseEntity<ContaResponseDTO> createConta(@Valid @RequestBody ContaRequestDTO dto, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ContaResponseDTO createdConta = contaService.createConta(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConta);
    }

    @Operation(summary = "Listar contas paginadas", description = "Retorna contas do usuário com filtros opcionais", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<Page<ContaResponseDTO>> getContasUsuario(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false)StatusConta status) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<ContaResponseDTO> contas = contaService.getContasFiltradasPaginadas(userId, page, size, mes, ano, status);

        return ResponseEntity.ok(contas);
    }

    @Operation(summary = "Pagar conta", description = "Marca conta como paga e cria nova se for recorrente", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Conta paga com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "403", description = "Conta não pertence ao usuário")
    })
    @PutMapping("/{id}/pagar")
    public ResponseEntity<ContaResponseDTO> pagarConta(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ContaResponseDTO contaPaga = contaService.pagarConta(id, userId);
        return ResponseEntity.ok(contaPaga);
    }

    @Operation(summary = "Contas vencendo hoje", description = "Lista todas as contas que vencem hoje (admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/vencendo-hoje")
    public ResponseEntity<List<ContaResponseDTO>> contasVencendoHoje() {
        return ResponseEntity.ok(contaService.contasVencendoHoje());
    }

    @Operation(summary = "Testar envio de email", description = "Endpoint de teste para verificar notificações", security = @SecurityRequirement(name = "bearerAuth"))
    @Hidden
    @GetMapping("/test-email")
    public ResponseEntity<String> testarEmail() {
        notificationScheduler.enviarLembretesVencimento();
        return ResponseEntity.ok("Emails enviados!");
    }
}
