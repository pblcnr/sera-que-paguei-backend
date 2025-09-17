package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.exception.custom.ResourceNotFoundException;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Arrange
        Long userId = 1L;
        User userFalso = new User();
        userFalso.setId(userId);
        userFalso.setNome("João");
        userFalso.setEmail("joao@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userFalso));

        // Act
        UserResponseDTO resultado = userService.getUserByIdDTO(userId);

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deveLancarExececaoQuandoUsuarioNaoExiste() {
        // Arrange
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByIdDTO(userId);
        });

        verify(userRepository, times(1)).findById(userId);
    }


}
