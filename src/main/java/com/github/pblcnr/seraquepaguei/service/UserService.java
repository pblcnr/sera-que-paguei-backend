package com.github.pblcnr.seraquepaguei.service;


import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserUpdateDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.exception.custom.DuplicateResourceException;
import com.github.pblcnr.seraquepaguei.exception.custom.ResourceNotFoundException;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUserWithDTO(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email já cadastrado: " + dto.getEmail());
        }

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));

        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromEntity(savedUser);
    }

    public List<UserResponseDTO> getAllUsersDTO() {
        List<User> users = userRepository.findByAtivoTrue();
        List<UserResponseDTO> dtos = users.stream()
                .map(user -> UserResponseDTO.fromEntity(user))
                .toList();

        return dtos;
    }

    public UserResponseDTO getUserByIdDTO(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getAtivo())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + userId + " não encontrado"));

        return UserResponseDTO.fromEntity(user);
    }

    public UserResponseDTO updateUserDTO(Long userId, UserUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + userId + " não encontrado"));

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            user.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            if (!dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Email já cadastrado: " + dto.getEmail());
            }

            user.setEmail(dto.getEmail());
        }

        if (dto.getSenha() != null) {
            user.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    public void softDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + userId + " não encontrado"));

        user.setAtivo(false);
        userRepository.save(user);
    }

    public boolean validatePassword(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(rawPassword, user.getSenha());
    }
}
