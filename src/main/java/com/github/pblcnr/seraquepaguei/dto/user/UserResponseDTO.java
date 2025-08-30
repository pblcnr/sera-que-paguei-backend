package com.github.pblcnr.seraquepaguei.dto.user;

import com.github.pblcnr.seraquepaguei.entity.User;

public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public static UserResponseDTO fromEntity(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User não pode ser nulo");
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
