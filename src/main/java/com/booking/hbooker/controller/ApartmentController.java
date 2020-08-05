package com.booking.hbooker.controller;

import com.booking.hbooker.entities.Apartment;
import com.booking.hbooker.entities.Hotel;
import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import com.booking.hbooker.repos.ApartmentRepo;
import com.booking.hbooker.repos.BookingRepo;
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
import java.util.Map;

@Controller
@RequestMapping("/apartment")
public class ApartmentController {

    @Autowired
    private ApartmentRepo ApartmentRepo;

    @Autowired
    private HotelRepo HotelRepo;

    @GetMapping("/apartmentsList")
    public String apartmentsList(Model model,
                                 @PageableDefault(size = 5) Pageable pageable,
                                 @AuthenticationPrincipal User user) {

        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);
        model.addAttribute("isAdmin"        , IsAdmin);
        model.addAttribute("name"           , user.getUsername());

        if (IsAdmin) {
            model.addAttribute("page" , ApartmentRepo.findAll(pageable));
        }else{
            model.addAttribute("page" , ApartmentRepo.findAllByDeletionMark(false, pageable));
        };

        model.addAttribute("pageurl"    , "/apartment/apartmentsList");

        return "apartmentsList";

    }

    @GetMapping("{apartment}")
    public String apartmentEditForm(@PathVariable Apartment apartment,
                                    Model model,
                                    @AuthenticationPrincipal User user) {

        model.addAttribute("hotels"     , HotelRepo.findAll());
        model.addAttribute("apartment"  , apartment);
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "apartmentEdit";

    }

    @PostMapping("/apartmentedit")
    public String apartmentSave(
            @RequestParam String aprtname,
            @RequestParam String aptDescription,
            @RequestParam Map<String, String> form,
            @RequestParam("aprtId") Apartment apartment, @RequestParam("hotelid") Hotel hotel
    ) {
        apartment.setAprtname(aprtname);
        apartment.setHotel(hotel);
        apartment.setAptDescription(aptDescription);

        ApartmentRepo.save(apartment);

        return "redirect:/apartment/apartmentsList";
    }

    @GetMapping("{apartment}/apartmentdelete")
    public String aparmentDelete(
            @PathVariable Apartment apartment, Model model
    ) {

        if(apartment.isDeletionMark()){
            apartment.setDeletionMark(false);
        }else{
            apartment.setDeletionMark(true);
        };

        ApartmentRepo.save(apartment);

        //ApartmentRepo.deleteById(apartment.getId());

        return "redirect:/apartment/apartmentsList";
    }

    @GetMapping("/apartmentadd")
    public String apartmentAdd(Model model,
                               @AuthenticationPrincipal User user) {
        model.addAttribute("hotels"     , HotelRepo.findAll());
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "apartmentAdd";
    }

    @PostMapping("/apartmentadd")
    public String apartmentCreate(@Valid Apartment apartment,
                                  BindingResult result,
                                  String name,
                                  Model model,
                                  @RequestParam("hotelid") Hotel hotel,
                                  @AuthenticationPrincipal User user) {
        Apartment aptFromDb = ApartmentRepo.findByAprtname(apartment.getAprtname());

        model.addAttribute("isAdmin"        , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"           , user.getUsername());

        if (result.hasErrors()){
            model.addAttribute("message"    , "Please, enter apartmentname!");
            model.addAttribute("hotels"     , HotelRepo.findAll());

            return "apartmentAdd";
        };

        if (aptFromDb != null) {
            model.addAttribute("hotels"     , HotelRepo.findAll());
            model.addAttribute("message"    , "Apartment exists!");

            return "apartmentAdd";
        }

        apartment.setHotel(hotel);
        ApartmentRepo.save(apartment);

        return "redirect:/apartment/apartmentsList";
    }
}
