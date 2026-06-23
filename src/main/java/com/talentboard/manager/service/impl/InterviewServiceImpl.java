package com.talentboard.manager.service.impl;

import com.talentboard.manager.dto.request.InterviewRequestDTO;
import com.talentboard.manager.dto.response.InterviewResponseDTO;
import com.talentboard.manager.exception.BadRequestException;
import com.talentboard.manager.exception.ResourceNotFoundException;
import com.talentboard.manager.mapper.InterviewMapper;
import com.talentboard.manager.model.entity.Application;
import com.talentboard.manager.model.entity.Interview;
import com.talentboard.manager.model.entity.User;
import com.talentboard.manager.repository.ApplicationRepository;
import com.talentboard.manager.repository.InterviewRepository;
import com.talentboard.manager.service.InterviewService;
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
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewMapper interviewMapper;
    private final UserService userService;

    @Override
    @Transactional
    public InterviewResponseDTO schedule(InterviewRequestDTO dto) {
        // Business rule: interview datetime must be in the future
        LocalDateTime scheduledAt = LocalDateTime.of(dto.getInterviewDate(), dto.getInterviewTime());
        if (!scheduledAt.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Interview must be scheduled at a future date and time.");
        }

        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", dto.getApplicationId()));

        User responsible = userService.findEntityById(dto.getResponsibleId());

        Interview interview = Interview.builder()
                .application(application)
                .responsible(responsible)
                .interviewDate(dto.getInterviewDate())
                .interviewTime(dto.getInterviewTime())
                .type(dto.getType())
                .observations(dto.getObservations())
                .build();

        Interview saved = interviewRepository.save(interview);
        log.info("Interview scheduled for application {} on {} {}", dto.getApplicationId(),
                dto.getInterviewDate(), dto.getInterviewTime());
        return interviewMapper.toResponseDTO(saved);
    }

    @Override
    public InterviewResponseDTO findById(Long id) {
        return interviewMapper.toResponseDTO(findInterviewEntity(id));
    }

    @Override
    public Page<InterviewResponseDTO> findByApplication(Long applicationId, Pageable pageable) {
        return interviewRepository.findByApplicationIdApplication(applicationId, pageable)
                .map(interviewMapper::toResponseDTO);
    }

    @Override
    public Page<InterviewResponseDTO> findByResponsible(String responsibleEmail, Pageable pageable) {
        User responsible = userService.findEntityByEmail(responsibleEmail);
        return interviewRepository.findByResponsibleIdUser(responsible.getIdUser(), pageable)
                .map(interviewMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Interview interview = findInterviewEntity(id);
        interviewRepository.delete(interview);
        log.info("Interview {} deleted", id);
    }

    private Interview findInterviewEntity(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", id));
    }
}
