package com.talentboard.manager.service;

import com.talentboard.manager.dto.request.ApplicationRequestDTO;
import com.talentboard.manager.dto.response.ApplicationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

    ApplicationResponseDTO apply(ApplicationRequestDTO dto, String candidateEmail);

    ApplicationResponseDTO findById(Long id);

    Page<ApplicationResponseDTO> findByCandidate(String candidateEmail, Pageable pageable);

    Page<ApplicationResponseDTO> findByVacancy(Long vacancyId, Pageable pageable);

    void updateStatus(Long id, String newStatus, String requesterEmail);
}
