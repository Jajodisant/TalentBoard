package com.talentboard.manager.service;

import com.talentboard.manager.dto.request.RegisterRequestDTO;
import com.talentboard.manager.dto.response.UserResponseDTO;
import com.talentboard.manager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO dto);

    UserResponseDTO findById(Long id);

    User findEntityByEmail(String email);

    User findEntityById(Long id);

    Page<UserResponseDTO> findAll(String search, Pageable pageable);
}
