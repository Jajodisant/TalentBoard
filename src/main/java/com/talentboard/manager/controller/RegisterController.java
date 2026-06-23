package com.talentboard.manager.controller;

import com.talentboard.manager.dto.request.RegisterRequestDTO;
import com.talentboard.manager.model.enums.Role;
import com.talentboard.manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterRequestDTO());
        model.addAttribute("roles", Role.values());
        return "public/register";
    }

    @PostMapping
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterRequestDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "public/register";
        }

        try {
            userService.register(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully. Please sign in.");
            return "redirect:/login";
        } catch (Exception ex) {
            log.warn("Registration failed: {}", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("roles", Role.values());
            return "public/register";
        }
    }
}
