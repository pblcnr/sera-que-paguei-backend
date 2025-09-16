package com.github.pblcnr.seraquepaguei.controller;

import com.github.pblcnr.seraquepaguei.dto.auth.LoginRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.auth.LoginResponseDTO;
import com.github.pblcnr.seraquepaguei.dto.auth.RegisterRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints de autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Registrar novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou senhas não conferem"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        if (!dto.getSenha().equals(dto.getConfirmaSenha())) {
            return ResponseEntity.badRequest().body("Senhas não conferem");
        }

        UserResponseDTO user = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Login de usuário registrado")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
