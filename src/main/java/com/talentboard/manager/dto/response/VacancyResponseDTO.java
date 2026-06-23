package com.talentboard.manager.dto.response;

import com.talentboard.manager.model.enums.VacancyStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class VacancyResponseDTO {

    Long idVacancy;
    String title;
    String description;
    String category;
    String workMode;
    BigDecimal salary;
    LocalDateTime publicationDate;
    VacancyStatus status;
    String responsibleName;
    Long responsibleId;
}
