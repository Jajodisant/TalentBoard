package com.talentboard.manager.model.entity;

import com.talentboard.manager.model.enums.AppStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_application")
    private Long idApplication;

    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_vacancy")
    private Vacancy vacancy;
}
