package com.talentboard.manager.controller;

import com.talentboard.manager.dto.request.VacancyRequestDTO;
import com.talentboard.manager.dto.response.VacancyResponseDTO;
import com.talentboard.manager.model.enums.VacancyStatus;
import com.talentboard.manager.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
@Slf4j
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<VacancyResponseDTO> vacancies = vacancyService.findByStatus(
                VacancyStatus.OPEN, PageRequest.of(page, size, Sort.by("publicationDate").descending()));
        model.addAttribute("vacancies", vacancies);
        return "private/vacancy/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("vacancy", vacancyService.findById(id));
        return "private/vacancy/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("vacancyForm", new VacancyRequestDTO());
        model.addAttribute("statuses", VacancyStatus.values());
        return "private/vacancy/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("vacancyForm") VacancyRequestDTO dto,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", VacancyStatus.values());
            return "private/vacancy/form";
        }
        VacancyResponseDTO created = vacancyService.create(dto, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Vacancy created successfully.");
        return "redirect:/vacancies/" + created.getIdVacancy();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        VacancyResponseDTO vacancy = vacancyService.findById(id);
        VacancyRequestDTO form = new VacancyRequestDTO();
        form.setTitle(vacancy.getTitle());
        form.setDescription(vacancy.getDescription());
        form.setCategory(vacancy.getCategory());
        form.setWorkMode(vacancy.getWorkMode());
        form.setSalary(vacancy.getSalary());
        form.setStatus(vacancy.getStatus());
        model.addAttribute("vacancyForm", form);
        model.addAttribute("vacancyId", id);
        model.addAttribute("statuses", VacancyStatus.values());
        return "private/vacancy/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("vacancyForm") VacancyRequestDTO dto,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vacancyId", id);
            model.addAttribute("statuses", VacancyStatus.values());
            return "private/vacancy/form";
        }
        vacancyService.update(id, dto, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Vacancy updated successfully.");
        return "redirect:/vacancies/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        vacancyService.delete(id, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Vacancy deleted.");
        return "redirect:/vacancies";
    }
}
