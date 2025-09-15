package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;
import com.github.pblcnr.seraquepaguei.exception.custom.BusinessException;
import com.github.pblcnr.seraquepaguei.exception.custom.ResourceNotFoundException;
import com.github.pblcnr.seraquepaguei.repository.ContaRepository;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;

    public ContaResponseDTO createConta(ContaRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + userId + " não encontrado"));

        if (dto.getDataVencimento().isBefore(LocalDate.now())) {
            throw new BusinessException("Data de vencimento não pode ser no passado");
        }

        Conta conta = new Conta();
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setUsuario(user);

        Conta savedConta = contaRepository.save(conta);

        return ContaResponseDTO.fromEntity(savedConta);
    }

    public List<ContaResponseDTO> getContasUsuario(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + userId + " não encontrado"));

        Page<Conta> conta = contaRepository.findByUsuario(user, PageRequest.of(0, 10));

        List<ContaResponseDTO> dtos = conta.stream()
                .map(c -> ContaResponseDTO.fromEntity(c))
                .toList();

        return dtos;
    }

    public ContaResponseDTO pagarConta(Long contaId, Long userId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + contaId + " não encontrada"));

        if (!conta.getUsuario().getId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para acessar esta conta");
        }

        conta.setDataPagamento(LocalDate.now());
        conta.setStatus(StatusConta.PAGA);

        if (conta.getRecorrente() != null && conta.getRecorrente()) {
            Conta novaConta = new Conta();
            novaConta.setDescricao(conta.getDescricao());
            novaConta.setValor(conta.getValor());
            novaConta.setDataVencimento(conta.getDataVencimento().plusMonths(1));
            novaConta.setUsuario(conta.getUsuario());
            novaConta.setCategoria(conta.getCategoria());
            novaConta.setRecorrente(true);
            contaRepository.save(novaConta);
        }

        Conta savedConta = contaRepository.save(conta);

        return ContaResponseDTO.fromEntity(savedConta);
    }

    public List<ContaResponseDTO> contasVencendoHoje() {
        List<Conta> conta = contaRepository.findByDataVencimentoAndStatus(LocalDate.now(), StatusConta.PENDENTE);

        List<ContaResponseDTO> dtos = conta.stream()
                .map(c -> ContaResponseDTO.fromEntity(c))
                .toList();

        return dtos;
    }

    public List<Conta> getContasVencendoHojeComUsuario() {
        return contaRepository.findByDataVencimentoAndStatus(LocalDate.now(), StatusConta.PENDENTE);
    }

    public Page<ContaResponseDTO> getContasFiltradasPaginadas(Long userId, int page, int size, Integer mes, Integer ano, StatusConta status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Conta> contasPage;

        if (mes != null && ano != null) {
            LocalDate inicio = LocalDate.of(ano, mes, 1);
            LocalDate fim = inicio.plusMonths(1).minusDays(1);
            contasPage = contaRepository.findByUsuarioAndDataVencimentoBetween(user, inicio, fim, pageable);
        } else if (status != null) {
            contasPage = contaRepository.findByUsuarioAndStatus(user, status, pageable);
        } else {
            contasPage = contaRepository.findByUsuario(user, pageable);
        }

        return contasPage.map(c -> ContaResponseDTO.fromEntity(c));
    }
}
