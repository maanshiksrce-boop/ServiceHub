package com.servicehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "index";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("activePage", "services");
        return "services";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        model.addAttribute("activePage", "booking");
        return "booking";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login?required=true";
        }
        model.addAttribute("activePage", "bookings");
        return "my-bookings";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login?required=true";
        }
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/my-bookings";
        }
        model.addAttribute("activePage", "admin");
        return "admin";
    }
}
