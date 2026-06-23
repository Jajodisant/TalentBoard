package com.talentboard.manager.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InterviewRequestDTO {

    @NotNull
    private Long applicationId;

    @NotNull
    private Long responsibleId;

    @NotNull
    private LocalDate interviewDate;

    @NotNull
    private LocalTime interviewTime;

    @Size(max = 50)
    private String type;

    private String observations;
}
