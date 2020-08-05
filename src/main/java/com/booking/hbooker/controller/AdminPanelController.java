package com.booking.hbooker.controller;

import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import com.booking.hbooker.repos.ApartmentRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPanelController {
    @GetMapping("/adminpanel")
    public String adminPanel( Model model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "adminPanel";
    }
}
