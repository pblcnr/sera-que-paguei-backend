package com.github.pblcnr.seraquepaguei.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarLembrete(String destinatario, String nomeConta, String valor, String vencimento) {
        System.out.println("========= EMAIL SIMULADO =========");
        System.out.println("Para: " + destinatario);
        System.out.println("Assunto: Lembrete - " + nomeConta + " vence hoje!");
        System.out.println("Conta: " + nomeConta);
        System.out.println("Valor: R$ " + valor);
        System.out.println("Vencimento: " + vencimento);
        System.out.println("==================================");

//            mailSender.send(message);
    }
}
