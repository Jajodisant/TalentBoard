package com.talentboard.manager.service.impl;

import com.talentboard.manager.dto.request.RegisterRequestDTO;
import com.talentboard.manager.dto.response.UserResponseDTO;
import com.talentboard.manager.exception.BadRequestException;
import com.talentboard.manager.exception.ResourceNotFoundException;
import com.talentboard.manager.mapper.UserMapper;
import com.talentboard.manager.model.entity.User;
import com.talentboard.manager.repository.UserRepository;
import com.talentboard.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("New user registered: {} with role {}", saved.getEmail(), saved.getRole());
        return userMapper.toResponseDTO(saved);
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Override
    public Page<UserResponseDTO> findAll(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return userRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable)
                    .map(userMapper::toResponseDTO);
        }
        return userRepository.findAll(pageable).map(userMapper::toResponseDTO);
    }
}
