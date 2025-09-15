package com.github.pblcnr.seraquepaguei.repository;

import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Page<Conta> findByUsuario(User usuario, Pageable pageable);

    Page<Conta> findByUsuarioAndStatus(User usuario, StatusConta status, Pageable pageable);

    List<Conta> findByDataVencimentoAndStatus(LocalDate data, StatusConta status);

    Page<Conta> findByUsuarioAndDataVencimentoBetween(
            User usuario,
            LocalDate inicio,
            LocalDate fim,
            Pageable pageable
    );

    boolean existsByUsuarioAndDescricaoAndDataVencimento(
            User usuario,
            String descricao,
            LocalDate dataVencimento
    );

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.usuario = :usuario " +
            "AND c.status = :status AND c.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalPagoPeriodo(User usuario, StatusConta status,
                                        LocalDate inicio, LocalDate fim);
}