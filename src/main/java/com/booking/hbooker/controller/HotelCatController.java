package com.booking.hbooker.controller;

import com.booking.hbooker.entities.HotelCat;
import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import com.booking.hbooker.repos.HotelCatRepo;
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
@RequestMapping("/hotcat")
public class HotelCatController {

    @Autowired
    private HotelCatRepo HotCatRepo;

    @GetMapping("/hotcatsList")
    public String hotcatList(Model model,
                             @PageableDefault(size = 5) Pageable pageable,
                             @AuthenticationPrincipal User user) {
        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        model.addAttribute("isAdmin", IsAdmin);

        if (IsAdmin) {
            model.addAttribute("page", HotCatRepo.findAll(pageable));
        }else{
            model.addAttribute("page", HotCatRepo.findAllByDeletionMark(false,pageable));
        };

        model.addAttribute("isAdmin"    , IsAdmin);
        model.addAttribute("pageurl"    , "/hotcat/hotcatsList");
        model.addAttribute("name"       , user.getUsername());

        return "hotcatsList";
    }

    @GetMapping("{hotcat}")
    public String hotcatEditForm(@PathVariable HotelCat hotcat,
                                 Model model,
                                 @AuthenticationPrincipal User user) {
        model.addAttribute("hotcat"     , hotcat);
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "hotcatsEdit";
    }

    @PostMapping("/hotcatedit")
    public String hotcatSave(
            @RequestParam String catname,
            @RequestParam Map<String, String> form,
            @RequestParam("hotelcatId") HotelCat hotcat
    ) {
        hotcat.setCatname(catname);

        HotCatRepo.save(hotcat);

        return "redirect:/hotcat/hotcatsList";
    }

    @GetMapping("{hotcat}/hotcatdelete")
    public String hotcatDelete(
            @PathVariable HotelCat hotcat, Model model
    ) {
        if (hotcat.isDeletionMark()){
            hotcat.setDeletionMark(false);
        }else{
            hotcat.setDeletionMark(true);
        };

        HotCatRepo.save(hotcat);

        //HotCatRepo.deleteById(hotcat.getId());

        return "redirect:/hotcat/hotList";
    }

    @GetMapping("/hotcatadd")
    public String hotcatAdd(  Model model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("isAdmin"    , user.getRoles().contains(Role.ADMIN));
        model.addAttribute("name"       , user.getUsername());

        return "hotcatsAdd";
    }

    @PostMapping("/hotcatladd")
    public String hotcatCreate(@Valid HotelCat hotcat,
                               BindingResult result,
                               String name,
                               Model model) {
        HotelCat hotcatFromDb = HotCatRepo.findByCatname(hotcat.getCatname());

        if (result.hasErrors()){
            model.addAttribute("message", "Please, enter hotcatname!");

            return "hotcatsAdd";
        };

        if (hotcatFromDb != null) {
            model.addAttribute("message", "Hotcat exists!");
            return "hotcatsAdd";
        }

        HotCatRepo.save(hotcat);

        return "redirect:/hotcat/hotcatsList";

    }

}
