package com.talentboard.manager.mapper;

import com.talentboard.manager.dto.request.VacancyRequestDTO;
import com.talentboard.manager.dto.response.VacancyResponseDTO;
import com.talentboard.manager.model.entity.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    @Mapping(source = "responsible.name", target = "responsibleName")
    @Mapping(source = "responsible.idUser", target = "responsibleId")
    VacancyResponseDTO toResponseDTO(Vacancy vacancy);

    @Mapping(target = "idVacancy", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "responsible", ignore = true)
    Vacancy toEntity(VacancyRequestDTO dto);

    @Mapping(target = "idVacancy", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "responsible", ignore = true)
    void updateEntity(VacancyRequestDTO dto, @MappingTarget Vacancy vacancy);
}
