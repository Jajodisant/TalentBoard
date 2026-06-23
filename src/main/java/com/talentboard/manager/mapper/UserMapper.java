package com.talentboard.manager.mapper;

import com.talentboard.manager.dto.response.UserResponseDTO;
import com.talentboard.manager.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);
}
