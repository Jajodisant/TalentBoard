package com.talentboard.manager.controller;

import com.talentboard.manager.model.enums.VacancyStatus;
import com.talentboard.manager.service.ApplicationService;
import com.talentboard.manager.service.UserService;
import com.talentboard.manager.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final VacancyService vacancyService;
    private final ApplicationService applicationService;
    private final UserService userService;

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        log.debug("Dashboard accessed by {}", email);

        if (hasRole(authentication, "ROLE_ADMIN")) {
            model.addAttribute("users", userService.findAll(null,
                    PageRequest.of(0, 10, Sort.by("createdAt").descending())));
            model.addAttribute("vacancies", vacancyService.findAll(
                    PageRequest.of(0, 10, Sort.by("publicationDate").descending())));
            return "private/admin/dashboard";
        }

        if (hasRole(authentication, "ROLE_RECRUITER")) {
            model.addAttribute("myVacancies", vacancyService.findByRecruiter(email,
                    PageRequest.of(0, 10, Sort.by("publicationDate").descending())));
            return "private/recruiter/dashboard";
        }

        // CANDIDATE
        model.addAttribute("myApplications", applicationService.findByCandidate(email,
                PageRequest.of(0, 10, Sort.by("applicationDate").descending())));
        model.addAttribute("openVacancies", vacancyService.findByStatus(VacancyStatus.OPEN,
                PageRequest.of(0, 10, Sort.by("publicationDate").descending())));
        return "private/candidate/dashboard";
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
}
