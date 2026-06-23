package com.talentboard.manager.controller;

import com.talentboard.manager.dto.request.ApplicationRequestDTO;
import com.talentboard.manager.dto.response.ApplicationResponseDTO;
import com.talentboard.manager.model.enums.AppStatus;
import com.talentboard.manager.service.ApplicationService;
import com.talentboard.manager.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;
    private final VacancyService vacancyService;

    @GetMapping
    public String myApplications(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Authentication authentication,
                                 Model model) {
        model.addAttribute("applications", applicationService.findByCandidate(
                authentication.getName(),
                PageRequest.of(page, size, Sort.by("applicationDate").descending())));
        return "private/application/list";
    }

    @GetMapping("/vacancy/{vacancyId}")
    public String byVacancy(@PathVariable Long vacancyId,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        model.addAttribute("applications", applicationService.findByVacancy(
                vacancyId, PageRequest.of(page, size, Sort.by("applicationDate").descending())));
        model.addAttribute("vacancy", vacancyService.findById(vacancyId));
        return "private/application/list";
    }

    @GetMapping("/apply/{vacancyId}")
    public String applyForm(@PathVariable Long vacancyId, Model model) {
        ApplicationRequestDTO form = new ApplicationRequestDTO();
        form.setVacancyId(vacancyId);
        model.addAttribute("applicationForm", form);
        model.addAttribute("vacancy", vacancyService.findById(vacancyId));
        return "private/application/apply";
    }

    @PostMapping("/apply")
    public String apply(@Valid @ModelAttribute("applicationForm") ApplicationRequestDTO dto,
                        BindingResult bindingResult,
                        Authentication authentication,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vacancy", vacancyService.findById(dto.getVacancyId()));
            return "private/application/apply";
        }
        applicationService.apply(dto, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully.");
        return "redirect:/applications";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ApplicationResponseDTO application = applicationService.findById(id);
        model.addAttribute("application", application);
        model.addAttribute("statuses", AppStatus.values());
        return "private/application/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        applicationService.updateStatus(id, status, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Application status updated.");
        return "redirect:/applications/" + id;
    }
}
