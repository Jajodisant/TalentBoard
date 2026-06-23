package com.talentboard.manager.service;

import com.talentboard.manager.dto.request.VacancyRequestDTO;
import com.talentboard.manager.dto.response.VacancyResponseDTO;
import com.talentboard.manager.model.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VacancyService {

    VacancyResponseDTO create(VacancyRequestDTO dto, String recruiterEmail);

    VacancyResponseDTO update(Long id, VacancyRequestDTO dto, String requesterEmail);

    void delete(Long id, String requesterEmail);

    VacancyResponseDTO findById(Long id);

    Page<VacancyResponseDTO> findAll(Pageable pageable);

    Page<VacancyResponseDTO> findByStatus(VacancyStatus status, Pageable pageable);

    Page<VacancyResponseDTO> findByRecruiter(String recruiterEmail, Pageable pageable);
}
