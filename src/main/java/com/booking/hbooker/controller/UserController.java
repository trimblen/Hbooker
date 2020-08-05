package com.booking.hbooker.controller;

import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import com.booking.hbooker.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/userList")
    public String userList(Model model,
                           @PageableDefault(size = 5) Pageable pageable,
                           @AuthenticationPrincipal User user) {
        model.addAttribute("page"       , userRepo.findAll(pageable));
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());
        model.addAttribute("pageurl"    , "/user/userList");

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user,
                               Model model,
                               @AuthenticationPrincipal User userlog) {
        model.addAttribute("user"       , user);
        model.addAttribute("roles"      , Role.values());
        model.addAttribute("isAdmin"    , userlog.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "userEdit";
    }

    @GetMapping("{user}/userdelete")
    public String userDelete(@PathVariable User user,
                             Model model,
                             @PageableDefault(size = 5) Pageable pageable,
                             @AuthenticationPrincipal User userlog) {
        userRepo.deleteById(user.getId());

        model.addAttribute("page"       , userRepo.findAll(pageable));
        model.addAttribute("isAdmin"    , userlog.getRoles().contains(Role.ADMIN));
        model.addAttribute("pageurl"    , "/user/userList");

        return "userList";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

        return "redirect:/user/userList";
    }
}
