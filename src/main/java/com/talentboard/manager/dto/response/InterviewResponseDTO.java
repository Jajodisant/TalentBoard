package com.talentboard.manager.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class InterviewResponseDTO {

    Long idInterview;
    LocalDate interviewDate;
    LocalTime interviewTime;
    String type;
    String result;
    String observations;
    Long applicationId;
    Long responsibleId;
    String responsibleName;
}
