package com.talentboard.manager.model.entity;

import com.talentboard.manager.model.enums.VacancyStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacancy")
    private Long idVacancy;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    private String workMode;

    @Column(nullable = true)
    private BigDecimal salary;

    private LocalDateTime publicationDate;

    @Enumerated(EnumType.STRING)
    private VacancyStatus status;

    @ManyToOne
    @JoinColumn(name = "fk_responsible")
    private User responsible;
}
