package com.talentboard.manager.mapper;

import com.talentboard.manager.dto.response.ApplicationResponseDTO;
import com.talentboard.manager.model.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "user.idUser", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "vacancy.idVacancy", target = "vacancyId")
    @Mapping(source = "vacancy.title", target = "vacancyTitle")
    ApplicationResponseDTO toResponseDTO(Application application);
}
