package com.booking.hbooker.controller;

import com.booking.hbooker.entities.Role;
import com.booking.hbooker.repos.BookingRepo;
import com.booking.hbooker.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.booking.hbooker.entities.User;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepo UserRepo;

    @Autowired
    private BookingRepo BookingRepo;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name",
            required=false,
            defaultValue="World") String name,
                           Model model,
                           @AuthenticationPrincipal User user) {
        List<User> usrsAdmin = UserRepo.findAllByRolesContains(Role.ADMIN);

        //create admin if not exists
        if (usrsAdmin.size() ==0){
            User newAdmin = new User();

            newAdmin.setActive(true);

            newAdmin.setUsername("admin");
            newAdmin.setPassword("admin");

            newAdmin.setRoles(Collections.singleton(Role.ADMIN));

            UserRepo.save(newAdmin);

            if (user!=null) {
                model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
                model.addAttribute("name", user.getUsername());
            }else{
                model.addAttribute("isAdmin"    , false);
                model.addAttribute("name"       , "guest");
            }
            return "greeting";
        };

        if (user!=null) {
            model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
            model.addAttribute("name"   , user.getUsername());
        }else{
            model.addAttribute("isAdmin"    , false);
            model.addAttribute("name"       , "guest");
        }

        return "greeting";

    }

    @GetMapping("/main")
    public String main(Model model,
                       @PageableDefault(size = 5) Pageable pageable,
                       @AuthenticationPrincipal User user) {
        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        if (IsAdmin){
            model.addAttribute("page"   , BookingRepo.findAll(pageable));
        }else{
            model.addAttribute("page"   , BookingRepo.findAllByAuthorId(user.getId(),pageable));

        };
        model.addAttribute("pageurl"    , "/main");
        model.addAttribute("isAdmin"    , IsAdmin);
        model.addAttribute("user"       , user);
        model.addAttribute("name"       , user.getUsername());

        return "main";
    }

//    @PostMapping("/main")
//    public String add(@AuthenticationPrincipal User user,@RequestParam String text, @RequestParam String tag, String name, Model model) {
//
//        Message message = new Message(text, tag, user);
//
//        messageRepo.save(message);
//
//        Iterable<Message> messages = messageRepo.findAll();
//
//        model.addAttribute("messages", messages);
//
//        return "main";
//
//    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);

        return "login";
    }

//    @PostMapping("filter")
//    public String filter(@RequestParam String filter, String name, Model model) {
//        Iterable<Message> messages;
//
//        if (filter != null && !filter.isEmpty()) {
//            messages = messageRepo.findByTag(filter);
//        } else {
//            messages = messageRepo.findAll();
//        }
//
//        model.addAttribute("messages", messages);
//
//        return "main";
//    }

}