package com.talentboard.manager.service;

import com.talentboard.manager.dto.request.InterviewRequestDTO;
import com.talentboard.manager.dto.response.InterviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewService {

    InterviewResponseDTO schedule(InterviewRequestDTO dto);

    InterviewResponseDTO findById(Long id);

    Page<InterviewResponseDTO> findByApplication(Long applicationId, Pageable pageable);

    Page<InterviewResponseDTO> findByResponsible(String responsibleEmail, Pageable pageable);

    void delete(Long id);
}
