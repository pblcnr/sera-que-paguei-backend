package com.github.pblcnr.seraquepaguei.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void deveGerarTokenValido() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "secretKey", "teste-secret-com-mais-de-32-caracteres-aqui");
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000L);

        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        // Act
        String[] partes = token.split("\\.");
        assertEquals(3, partes.length, "Token JWT deve ter 3 partes");

        // Assert
        assertFalse(partes[0].isEmpty(), "Header não pode estar vazio");
        assertFalse(partes[1].isEmpty(), "Payload não pode estar vazio");
        assertFalse(partes[2].isEmpty(), "Signature não pode estar vazio");
    }

    @Test
    void deveValidarTokenCorreto() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "secretKey", "teste-secret-com-mais-de-32-caracteres-aqui");
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000L);

        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        // Act
        boolean valido = jwtService.validateToken(token);

        // Assert
        assertTrue(valido, "Token válido deve retornar true");
    }

    @Test
    void deveRejeitarTokenInvalido() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "secretKey","teste-secret-com-mais-de-32-caracteres-aqui");
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000L);
        String tokenInvalido = "token.invalido";


        // Act
        boolean valido = jwtService.validateToken(tokenInvalido);

        // Assert
        assertFalse(valido, "Token inválido deve retornar false");
    }
}
