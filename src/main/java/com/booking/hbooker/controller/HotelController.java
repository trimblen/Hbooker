package com.booking.hbooker.controller;

import com.booking.hbooker.entities.Hotel;
import com.booking.hbooker.entities.HotelCat;
import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import com.booking.hbooker.repos.HotelCatRepo;
import com.booking.hbooker.repos.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelRepo HotelRepo;

    @Autowired
    private HotelCatRepo HotelCatRepo;

    @GetMapping("/hotelList")
    public String hotelsList(Model model,
                             @PageableDefault(size = 5) Pageable pageable,
                             @AuthenticationPrincipal User user) {
        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        model.addAttribute("isAdmin"    , IsAdmin);

        if (IsAdmin) {
            model.addAttribute("page"   , HotelRepo.findAll(pageable));
        } else{
            model.addAttribute("page"   , HotelRepo.findAllByDeletionMark(false,pageable));

        };

        model.addAttribute("name"       , user.getUsername());

        model.addAttribute("pageurl"    , "/hotel/hotelList");

        return "hotelList";
    }

    @GetMapping("{hotel}")
    public String hotelEditForm(@PathVariable Hotel hotel,
                                Model model,
                                @AuthenticationPrincipal User user) {
        model.addAttribute("hotel"      , hotel);
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("hotelcats"  , HotelCatRepo.findAll());
        model.addAttribute("name"       , user.getUsername());

        return "hotelEdit";
    }

    @PostMapping("/hoteledit")
    public String hotelSave(
            @RequestParam String hotelname,
            @RequestParam String hDescription,
            @RequestParam Map<String, String> form,
            @RequestParam("hotelId") Hotel hotel, @RequestParam("hotelcat")  HotelCat hotcat
    ) {
        hotel.setHotelcat(hotcat);
        hotel.sethDescription(hDescription);
        hotel.setHotelname(hotelname);

        HotelRepo.save(hotel);

        return "redirect:/hotel/hotelList";
    }

    @GetMapping("{hotel}/hoteldelete")
    public String hotelDelete(
            @PathVariable Hotel hotel, Model model
    ) {
        if (hotel.isDeletionMark()){
            hotel.setDeletionMark(false);
        }else{
            hotel.setDeletionMark(true);
        };

        HotelRepo.save(hotel);

        //HotelRepo.deleteById(hotel.getId());

        return "redirect:/hotel/hotelList";
    }

    @GetMapping("/hoteladd")
    public String hoteladd(Model model,
                           @AuthenticationPrincipal User user) {
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("hotelcats"  , HotelCatRepo.findAll());
        model.addAttribute("name"       , user.getUsername());

        return "hotelAdd";
    }

    @PostMapping("/hoteladd")
    public String hotelCreate(@Valid Hotel hotel,
                              BindingResult result,
                              String name,
                              Model model,
                              @RequestParam("hotelcat")  HotelCat hotcat,
                              @AuthenticationPrincipal User user) {
        Hotel hotelFromDb = HotelRepo.findByHotelname(hotel.getHotelname());

        model.addAttribute("hotelcats"  , HotelCatRepo.findAll());
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));

        if (result.hasErrors()){
            model.addAttribute("message", "Please, enter hotelname!");

            return "hotelAdd";
        };

        if (hotelFromDb != null) {
            model.addAttribute("message"    , "Hotel exists!");

            return "hotelAdd";

        }

        hotel.setHotelcat(hotcat);

        HotelRepo.save(hotel);

        return "redirect:/hotel/hotelList";
    }
}
