package com.talentboard.manager.controller;

import com.talentboard.manager.dto.request.InterviewRequestDTO;
import com.talentboard.manager.dto.response.InterviewResponseDTO;
import com.talentboard.manager.service.ApplicationService;
import com.talentboard.manager.service.InterviewService;
import com.talentboard.manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/interviews")
@RequiredArgsConstructor
@Slf4j
public class InterviewController {

    private final InterviewService interviewService;
    private final ApplicationService applicationService;
    private final UserService userService;

    @GetMapping
    public String myInterviews(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Authentication authentication,
                               Model model) {
        model.addAttribute("interviews", interviewService.findByResponsible(
                authentication.getName(),
                PageRequest.of(page, size, Sort.by("interviewDate").descending())));
        return "private/interview/list";
    }

    @GetMapping("/application/{applicationId}")
    public String byApplication(@PathVariable Long applicationId,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        model.addAttribute("interviews", interviewService.findByApplication(
                applicationId, PageRequest.of(page, size, Sort.by("interviewDate").ascending())));
        model.addAttribute("application", applicationService.findById(applicationId));
        return "private/interview/list";
    }

    @GetMapping("/schedule")
    public String scheduleForm(@RequestParam(required = false) Long applicationId,
                               Authentication authentication,
                               Model model) {
        InterviewRequestDTO form = new InterviewRequestDTO();
        if (applicationId != null) {
            form.setApplicationId(applicationId);
        }
        model.addAttribute("interviewForm", form);
        if (applicationId != null) {
            model.addAttribute("application", applicationService.findById(applicationId));
        }
        return "private/interview/form";
    }

    @PostMapping("/schedule")
    public String schedule(@Valid @ModelAttribute("interviewForm") InterviewRequestDTO dto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (dto.getApplicationId() != null) {
                model.addAttribute("application", applicationService.findById(dto.getApplicationId()));
            }
            return "private/interview/form";
        }
        InterviewResponseDTO created = interviewService.schedule(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Interview scheduled successfully.");
        return "redirect:/interviews/" + created.getIdInterview();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("interview", interviewService.findById(id));
        return "private/interview/detail";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        interviewService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Interview deleted.");
        return "redirect:/interviews";
    }
}
