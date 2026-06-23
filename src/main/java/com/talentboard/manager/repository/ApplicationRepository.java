package com.talentboard.manager.repository;

import com.talentboard.manager.model.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByVacancyIdVacancyAndUserIdUser(Long idVacancy, Long idUser);

    Page<Application> findByUserIdUser(Long idUser, Pageable pageable);

    Page<Application> findByVacancyIdVacancy(Long idVacancy, Pageable pageable);
}
