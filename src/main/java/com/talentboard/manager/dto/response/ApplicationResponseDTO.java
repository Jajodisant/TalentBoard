package com.talentboard.manager.dto.response;

import com.talentboard.manager.model.enums.AppStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ApplicationResponseDTO {

    Long idApplication;
    LocalDateTime applicationDate;
    AppStatus status;
    String observations;
    Long userId;
    String userName;
    Long vacancyId;
    String vacancyTitle;
}
