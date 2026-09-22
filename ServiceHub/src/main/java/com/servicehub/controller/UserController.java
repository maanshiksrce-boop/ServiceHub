package com.servicehub.controller;

import com.servicehub.model.LoginForm;
import com.servicehub.model.RegistrationForm;
import com.servicehub.model.User;
import com.servicehub.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        if (!model.containsAttribute("registration")) {
            model.addAttribute("registration", new RegistrationForm());
        }
        model.addAttribute("activePage", "register");
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registration") RegistrationForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "register");
            return "register";
        }
        try {
            userService.register(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("email", "email.duplicate", exception.getMessage());
            model.addAttribute("activePage", "register");
            return "register";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Account created successfully. Please sign in.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String required, Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        if (required != null) {
            model.addAttribute("errorMessage", "Please sign in to view your bookings.");
        }
        model.addAttribute("activePage", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "login");
            return "login";
        }
        User user = userService.authenticate(form.getEmail(), form.getPassword()).orElse(null);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
            model.addAttribute("activePage", "login");
            return "login";
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole().name());
        return "redirect:/my-bookings";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been signed out.");
        return "redirect:/login";
    }
}
