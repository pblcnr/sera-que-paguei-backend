package com.github.pblcnr.seraquepaguei.repository;

import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByUsuario(User usuario);

    List<Conta> findByUsuarioAndStatus(User usuario, StatusConta status);

    List<Conta> findByDataVencimentoAndStatus(LocalDate data, StatusConta status);

    List<Conta> findByUsuarioAndDataVencimentoBetween(
            User usuario,
            LocalDate inicio,
            LocalDate fim
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