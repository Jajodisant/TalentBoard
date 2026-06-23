package com.talentboard.manager.mapper;

import com.talentboard.manager.dto.response.InterviewResponseDTO;
import com.talentboard.manager.model.entity.Interview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    @Mapping(source = "application.idApplication", target = "applicationId")
    @Mapping(source = "responsible.idUser", target = "responsibleId")
    @Mapping(source = "responsible.name", target = "responsibleName")
    InterviewResponseDTO toResponseDTO(Interview interview);
}
