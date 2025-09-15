package com.github.pblcnr.seraquepaguei.scheduler;

import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.repository.ContaRepository;
import com.github.pblcnr.seraquepaguei.service.ContaService;
import com.github.pblcnr.seraquepaguei.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private ContaService contaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContaRepository contaRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void enviarLembretesVencimento() {
        List<Conta> contas = contaService.getContasVencendoHojeComUsuario();

        for (Conta conta : contas) {
            if (conta.getNotificadoEm() == null || !conta.getNotificadoEm().toLocalDate().equals(LocalDate.now())) {

                emailService.enviarLembrete(
                        conta.getUsuario().getEmail(),
                        conta.getDescricao(),
                        conta.getValor().toString(),
                        conta.getDataVencimento().toString()
                );

                conta.setNotificadoEm(LocalDateTime.now());
                contaRepository.save(conta);
            }
        }
    }
}
