package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.conta.ContaRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.conta.ContaResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;
import com.github.pblcnr.seraquepaguei.repository.ContaRepository;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;

    public ContaResponseDTO createConta(ContaRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getDataVencimento().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data de vencimento não pode ser no passado");
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Conta> conta = contaRepository.findByUsuario(user);

        List<ContaResponseDTO> dtos = conta.stream()
                .map(c -> ContaResponseDTO.fromEntity(c))
                .toList();

        return dtos;
    }

    public ContaResponseDTO pagarConta(Long contaId, Long userId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (!conta.getUsuario().getId().equals(userId)) {
            throw new RuntimeException("Conta não pertence ao usuário");
        }

        conta.setDataPagamento(LocalDate.now());
        conta.setStatus(StatusConta.PAGA);

        if (conta.getRecorrente()) {
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
}
