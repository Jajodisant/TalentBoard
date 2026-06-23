package com.talentboard.manager.dto.request;

import com.talentboard.manager.model.enums.VacancyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VacancyRequestDTO {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String description;

    @Size(max = 100)
    private String category;

    @Size(max = 50)
    private String workMode;

    private BigDecimal salary;

    @NotNull
    private VacancyStatus status;
}
