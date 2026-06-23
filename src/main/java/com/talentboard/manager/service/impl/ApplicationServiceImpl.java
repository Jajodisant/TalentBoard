package com.talentboard.manager.service.impl;

import com.talentboard.manager.dto.request.ApplicationRequestDTO;
import com.talentboard.manager.dto.response.ApplicationResponseDTO;
import com.talentboard.manager.exception.BadRequestException;
import com.talentboard.manager.exception.ResourceNotFoundException;
import com.talentboard.manager.mapper.ApplicationMapper;
import com.talentboard.manager.model.entity.Application;
import com.talentboard.manager.model.entity.User;
import com.talentboard.manager.model.entity.Vacancy;
import com.talentboard.manager.model.enums.AppStatus;
import com.talentboard.manager.model.enums.Role;
import com.talentboard.manager.model.enums.VacancyStatus;
import com.talentboard.manager.repository.ApplicationRepository;
import com.talentboard.manager.repository.VacancyRepository;
import com.talentboard.manager.service.ApplicationService;
import com.talentboard.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;

    @Override
    @Transactional
    public ApplicationResponseDTO apply(ApplicationRequestDTO dto, String candidateEmail) {
        User candidate = userService.findEntityByEmail(candidateEmail);

        Vacancy vacancy = vacancyRepository.findById(dto.getVacancyId())
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", dto.getVacancyId()));

        // Business rule: no applications to closed vacancies
        if (vacancy.getStatus() == VacancyStatus.CLOSED) {
            throw new BadRequestException("Cannot apply to a closed vacancy.");
        }

        // Business rule: no duplicate applications
        if (applicationRepository.existsByVacancyIdVacancyAndUserIdUser(
                vacancy.getIdVacancy(), candidate.getIdUser())) {
            throw new BadRequestException("You have already applied to this vacancy.");
        }

        Application application = Application.builder()
                .user(candidate)
                .vacancy(vacancy)
                .applicationDate(LocalDateTime.now())
                .status(AppStatus.APPLIED)
                .observations(dto.getObservations())
                .build();

        Application saved = applicationRepository.save(application);
        log.info("Candidate {} applied to vacancy {}", candidateEmail, vacancy.getIdVacancy());
        return applicationMapper.toResponseDTO(saved);
    }

    @Override
    public ApplicationResponseDTO findById(Long id) {
        return applicationMapper.toResponseDTO(findApplicationEntity(id));
    }

    @Override
    public Page<ApplicationResponseDTO> findByCandidate(String candidateEmail, Pageable pageable) {
        User candidate = userService.findEntityByEmail(candidateEmail);
        return applicationRepository.findByUserIdUser(candidate.getIdUser(), pageable)
                .map(applicationMapper::toResponseDTO);
    }

    @Override
    public Page<ApplicationResponseDTO> findByVacancy(Long vacancyId, Pageable pageable) {
        return applicationRepository.findByVacancyIdVacancy(vacancyId, pageable)
                .map(applicationMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String newStatus, String requesterEmail) {
        Application application = findApplicationEntity(id);
        User requester = userService.findEntityByEmail(requesterEmail);

        // Only ADMIN or the responsible recruiter of the vacancy may update status
        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isRecruiter = requester.getRole() == Role.RECRUITER
                && application.getVacancy().getResponsible() != null
                && application.getVacancy().getResponsible().getIdUser().equals(requester.getIdUser());

        if (!isAdmin && !isRecruiter) {
            throw new BadRequestException("You do not have permission to update this application status.");
        }

        try {
            application.setStatus(AppStatus.valueOf(newStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status value: " + newStatus);
        }

        log.info("Application {} status updated to {} by {}", id, newStatus, requesterEmail);
    }

    private Application findApplicationEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }
}
