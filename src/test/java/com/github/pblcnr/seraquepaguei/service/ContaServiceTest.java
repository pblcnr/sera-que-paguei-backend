package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.exception.custom.BusinessException;
import com.github.pblcnr.seraquepaguei.repository.ContaRepository;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarContaComSucesso() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setNome("João");

        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setDescricao("Conta de Luz");
        dto.setValor(new BigDecimal("150.00"));
        dto.setDataVencimento(LocalDate.now().plusDays(5));

        Conta contaSalva = new Conta();
        contaSalva.setId(1L);
        contaSalva.setDescricao(dto.getDescricao());
        contaSalva.setValor(dto.getValor());
        contaSalva.setDataVencimento(dto.getDataVencimento());
        contaSalva.setUsuario(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(contaRepository.save(any(Conta.class))).thenReturn(contaSalva);

        // Act
        ContaResponseDTO resultado = contaService.createConta(dto, userId);

        //Assert
        assertNotNull(resultado);
        assertEquals("Conta de Luz", resultado.getDescricao());
        assertEquals(new BigDecimal("150.00"), resultado.getValor());

        verify(userRepository, times(1)).findById(userId);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void naoDeveCriarContaComDataPassada() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setNome("João");

        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setDescricao("Conta de Luz");
        dto.setValor(new BigDecimal("150.00"));
        dto.setDataVencimento(LocalDate.now().minusDays(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            contaService.createConta(dto, userId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(contaRepository, never()).save(any(Conta.class));
    }
}
