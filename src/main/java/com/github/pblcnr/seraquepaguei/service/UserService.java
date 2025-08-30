package com.github.pblcnr.seraquepaguei.service;


import com.github.pblcnr.seraquepaguei.dto.user.UserRequestDTO;
import com.github.pblcnr.seraquepaguei.dto.user.UserResponseDTO;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // TODO: Hashear senha antes de salvar
        // user.setSenha(passwordEncoder.encode(user.getSenha()));

        return userRepository.save(user);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(userDetails.getNome());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO createUserWithDTO(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());

        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromEntity(savedUser);
    }

    public List<UserResponseDTO> getAllUsersDTO() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> dtos = users.stream()
                .map(user -> UserResponseDTO.fromEntity(user))
                .toList();

        return dtos;
    }

    public UserResponseDTO getUserByIdDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return UserResponseDTO.fromEntity(user);
    }
}
