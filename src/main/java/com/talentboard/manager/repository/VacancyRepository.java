package com.talentboard.manager.repository;

import com.talentboard.manager.model.entity.Vacancy;
import com.talentboard.manager.model.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    Page<Vacancy> findByStatus(VacancyStatus status, Pageable pageable);

    Page<Vacancy> findByResponsibleIdUser(Long idUser, Pageable pageable);
}
