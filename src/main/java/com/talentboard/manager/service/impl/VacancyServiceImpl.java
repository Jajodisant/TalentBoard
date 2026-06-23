package com.talentboard.manager.service.impl;

import com.talentboard.manager.dto.request.VacancyRequestDTO;
import com.talentboard.manager.dto.response.VacancyResponseDTO;
import com.talentboard.manager.exception.BadRequestException;
import com.talentboard.manager.exception.ResourceNotFoundException;
import com.talentboard.manager.mapper.VacancyMapper;
import com.talentboard.manager.model.entity.User;
import com.talentboard.manager.model.entity.Vacancy;
import com.talentboard.manager.model.enums.Role;
import com.talentboard.manager.model.enums.VacancyStatus;
import com.talentboard.manager.repository.VacancyRepository;
import com.talentboard.manager.service.UserService;
import com.talentboard.manager.service.VacancyService;
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
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final VacancyMapper vacancyMapper;
    private final UserService userService;

    @Override
    @Transactional
    public VacancyResponseDTO create(VacancyRequestDTO dto, String recruiterEmail) {
        User recruiter = userService.findEntityByEmail(recruiterEmail);
        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancy.setResponsible(recruiter);
        vacancy.setPublicationDate(LocalDateTime.now());
        Vacancy saved = vacancyRepository.save(vacancy);
        log.info("Vacancy created: '{}' by {}", saved.getTitle(), recruiterEmail);
        return vacancyMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public VacancyResponseDTO update(Long id, VacancyRequestDTO dto, String requesterEmail) {
        Vacancy vacancy = findVacancyEntity(id);
        assertOwnerOrAdmin(vacancy, requesterEmail);
        vacancyMapper.updateEntity(dto, vacancy);
        log.info("Vacancy {} updated by {}", id, requesterEmail);
        return vacancyMapper.toResponseDTO(vacancy);
    }

    @Override
    @Transactional
    public void delete(Long id, String requesterEmail) {
        Vacancy vacancy = findVacancyEntity(id);
        assertOwnerOrAdmin(vacancy, requesterEmail);
        vacancyRepository.delete(vacancy);
        log.info("Vacancy {} deleted by {}", id, requesterEmail);
    }

    @Override
    public VacancyResponseDTO findById(Long id) {
        return vacancyMapper.toResponseDTO(findVacancyEntity(id));
    }

    @Override
    public Page<VacancyResponseDTO> findAll(Pageable pageable) {
        return vacancyRepository.findAll(pageable).map(vacancyMapper::toResponseDTO);
    }

    @Override
    public Page<VacancyResponseDTO> findByStatus(VacancyStatus status, Pageable pageable) {
        return vacancyRepository.findByStatus(status, pageable).map(vacancyMapper::toResponseDTO);
    }

    @Override
    public Page<VacancyResponseDTO> findByRecruiter(String recruiterEmail, Pageable pageable) {
        User recruiter = userService.findEntityByEmail(recruiterEmail);
        return vacancyRepository.findByResponsibleIdUser(recruiter.getIdUser(), pageable)
                .map(vacancyMapper::toResponseDTO);
    }

    // --- private helpers ---

    private Vacancy findVacancyEntity(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", id));
    }

    private void assertOwnerOrAdmin(Vacancy vacancy, String requesterEmail) {
        User requester = userService.findEntityByEmail(requesterEmail);
        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isOwner = vacancy.getResponsible() != null
                && vacancy.getResponsible().getIdUser().equals(requester.getIdUser());
        if (!isAdmin && !isOwner) {
            throw new BadRequestException("You do not have permission to modify this vacancy.");
        }
    }
}
