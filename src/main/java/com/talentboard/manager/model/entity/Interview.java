package com.talentboard.manager.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interview")
    private Long idInterview;

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    private String type;

    private String result;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "fk_application")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "fk_responsible")
    private User responsible;
}
