package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.auth.LoginRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.auth.LoginResponseDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserResponseDTO register(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        return userService.createUserWithDTO(dto);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!user.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        LoginResponseDTO response = new LoginResponseDTO();

        response.setToken(jwtService.generateToken(user.getId()));
        response.setUserId(user.getId());
        response.setNome(user.getNome());
        response.setEmail(user.getEmail());

        return response;
    }
}
