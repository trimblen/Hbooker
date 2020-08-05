package com.booking.hbooker.controller;

import com.booking.hbooker.entities.*;
import com.booking.hbooker.repos.ApartmentRepo;
import com.booking.hbooker.repos.BookingRepo;
import com.booking.hbooker.repos.HotelCatRepo;
import com.booking.hbooker.repos.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private ApartmentRepo ApartmentRepo;

    @Autowired
    private HotelRepo HotelRepo;

    @Autowired
    private HotelCatRepo HotelCatRepo;

    @Autowired
    private BookingRepo BookingRepo;

    @GetMapping("/bookingsList")
    public String bookingsList(Model model,
                               @PageableDefault(size = 5) Pageable pageable,
                               @AuthenticationPrincipal User user) {

        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        if (IsAdmin){
            model.addAttribute("page" , BookingRepo.findAll(pageable));
        }else{
            model.addAttribute("page" , BookingRepo.findAllByAuthorId(user.getId(),pageable));
        };

        model.addAttribute("hotelcats"  , HotelCatRepo.findAll());
        model.addAttribute("isAdmin"    , IsAdmin);
        model.addAttribute("name"       , user.getUsername());
        model.addAttribute("pageurl"    , "/booking/bookingsList");

        return "bookingsList";
    }

    @PostMapping("/bookingsearch")
    public String bookingSearch(
            @RequestParam Map<String, String>
                    form,
            @RequestParam("datetimebegin")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String datetimebegin,
            @RequestParam("datetimeend")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String datetimend,
            @RequestParam("hotcatlid")
                    HotelCat hotcat,
            Model model,
            @AuthenticationPrincipal User user) throws ParseException {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        if (IsAdmin){
            model.addAttribute("bookings"   , BookingRepo.findAll());
        }else{
            model.addAttribute("bookings"   , BookingRepo.findAllByAuthorId(user.getId()));
        };

        model.addAttribute("isAdmin"        , IsAdmin);
        model.addAttribute("datetimebegin"  , datetimebegin);
        model.addAttribute("datetimeend"    , datetimend);
        model.addAttribute("hotcatsel"      , hotcat);
        model.addAttribute("hotels"         , HotelRepo.findAll());
        model.addAttribute("hotelcats"      , HotelCatRepo.findAll());
        model.addAttribute("name"           , user.getUsername());

        if(datetimebegin=="" || datetimend==""){

            model.addAttribute("message"  , "Wrong date end or date begin!");

            return "bookingsList";

        };

        Date dtbegin                = formatter.parse(datetimebegin);
        Date dtend                  = formatter.parse(datetimend);

        if (dtend.compareTo(dtbegin)<0){

            model.addAttribute("message"  , "Datetime end is less than Datetime begin!");

            return "bookingsList";

        };

        List<Long> booked = BookingRepo.findByPrerequisites(dtbegin, dtend, hotcat.getId());

        if (booked.size()==0){
            List<Apartment> apartments = ApartmentRepo.findAllByDeletionMark(false);

            if(apartments.size()==0){
                model.addAttribute("message"  , "There are mo available apartments at the moment!");

                return "bookingsList";
            };

            model.addAttribute("apartments"  , apartments);
        }else{
            List<Apartment> apartments = ApartmentRepo.findByIdNotInAndDeletionMarkAndHotel_Hotelcat_Id(booked, false,hotcat.getId());

            if(apartments.size()==0){
                model.addAttribute("message"  , "There are mo available apartments at the moment!");

                return "bookingsList";
            };

            model.addAttribute("apartments"  , apartments);
        };

        return "bookingsList";
    }

    @GetMapping("{booking}/bookingdelete")
    public String bookingDelete(
            @PathVariable Booking booking, Model model
    ) {
        BookingRepo.deleteById(booking.getId());

        return "redirect:/booking/bookingsList";
    }

    @GetMapping("/bookingadd")
    public String bookingAdd(Model model) {
        model.addAttribute("hotels"     , HotelRepo.findAll());
        model.addAttribute("apartments" , ApartmentRepo.findAll());

        return "bookingAdd";
    }

    @PostMapping("/bookingadd")
    public String bookingCreate(Booking booking,
                                String name, Model model,
                                @RequestParam("bdatetimebegin")
                                @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            String bdatetimebegin,
                                @RequestParam("bdatetimeend")
                                @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            String bdatetimeend,
                                @RequestParam("aprtId") Apartment apartment,
                                @RequestParam("hotcatId") HotelCat hotcat,
                                @AuthenticationPrincipal User user) throws ParseException {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dtbegin                = formatter.parse(bdatetimebegin);
        Date dtend                  = formatter.parse(bdatetimeend);

        booking.setDatetimebegin(dtbegin);
        booking.setDatetimeend(dtend);
        booking.setApartment(apartment);
        booking.setHotcat(hotcat);
        booking.setHotel(apartment.getHotel());
        booking.setAuthor(user);

        BookingRepo.save(booking);

        boolean IsAdmin = user.getRoles().contains(Role.ADMIN);

        if (IsAdmin){

            model.addAttribute("bookings" , BookingRepo.findAll());

        }else{

            model.addAttribute("bookings" , BookingRepo.findAllByAuthorId(user.getId()));

        };

        model.addAttribute("isAdmin"        , IsAdmin);
        model.addAttribute("datetimebegin"  , bdatetimebegin);
        model.addAttribute("datetimeend"    , bdatetimeend);
        model.addAttribute("hotcatsel"      , hotcat);
        model.addAttribute("confirmed"      , "Booking was created succesfully! Check it, please, in the bookings list.");
        model.addAttribute("hotelcats"      , HotelCatRepo.findAll());

        return "bookingsList";
    }
}
