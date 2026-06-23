package com.talentboard.manager.controller;

import com.talentboard.manager.model.entity.User;
import com.talentboard.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthenticatedUserAdvice {

    private final UserService userService;

    @ModelAttribute("currentUser")
    public User addCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return userService.findEntityByEmail(authentication.getName());
        }
        return null;
    }
}
