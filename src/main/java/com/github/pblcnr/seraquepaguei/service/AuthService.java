package com.github.pblcnr.seraquepaguei.service;

import com.github.pblcnr.seraquepaguei.dto.auth.LoginRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.auth.LoginResponseDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.exception.custom.BusinessException;
import com.github.pblcnr.seraquepaguei.exception.custom.DuplicateResourceException;
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
            throw new DuplicateResourceException("Email já cadastrado: " + dto.getEmail());
        }

        return userService.createUserWithDTO(dto);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo. Entre em contato com o suporte");
        }

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new BusinessException("Email ou senha inválidos");
        }

        LoginResponseDTO response = new LoginResponseDTO();

        response.setToken(jwtService.generateToken(user.getId()));
        response.setUserId(user.getId());
        response.setNome(user.getNome());
        response.setEmail(user.getEmail());

        return response;
    }
}
