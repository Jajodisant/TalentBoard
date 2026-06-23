package com.talentboard.manager.repository;

import com.talentboard.manager.model.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Page<Interview> findByApplicationIdApplication(Long idApplication, Pageable pageable);

    Page<Interview> findByResponsibleIdUser(Long idUser, Pageable pageable);
}
