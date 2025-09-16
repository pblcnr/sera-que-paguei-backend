package com.github.pblcnr.seraquepaguei.controller;

import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserUpdateDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Listar todos usuários", description = "Retorna lista de usuários ativos", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersDTO());
    }

    @Operation(summary = "Buscar usuário por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        UserResponseDTO dto = userService.getUserByIdDTO(id);
        return ResponseEntity.ok(dto);

    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados do usuário (nome e email)", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {

        UserResponseDTO updatedUser = userService.updateUserDTO(id, dto);
        return ResponseEntity.ok(updatedUser);

    }

    @Operation(summary = "Desativar usuário", description = "Soft delete - marca usuário como inativo", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();

    }
}
