package com.talentboard.manager.dto.response;

import com.talentboard.manager.model.enums.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserResponseDTO {

    Long idUser;
    String name;
    String email;
    Role role;
    String status;
    LocalDateTime createdAt;
}
