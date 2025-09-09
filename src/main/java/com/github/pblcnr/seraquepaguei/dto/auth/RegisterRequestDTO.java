package com.github.pblcnr.seraquepaguei.dto.auth;

import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDTO extends UserRequestDTO {

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmaSenha;

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }
}
