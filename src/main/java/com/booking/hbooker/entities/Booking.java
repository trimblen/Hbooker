package com.booking.hbooker.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "booking_Sequence")
    @SequenceGenerator(name = "booking_Sequence", sequenceName = "BOOKING_SEQ")
    private Long id;

    @Column(name = "datetimebegin")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datetimebegin;

    @Column(name = "datetimeend")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datetimeend;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    Apartment apartment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotcat_id")
    private HotelCat hotcat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Booking(){

    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelCat getHotcat() {
        return hotcat;
    }

    public void setHotcat(HotelCat hotcat) {
        this.hotcat = hotcat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public String getOrderIdBooking(){
        DecimalFormat myFormatter = new DecimalFormat("ORD000000");
        return myFormatter.format(this.id);
    }

    public Date getDatetimebegin() {
        return datetimebegin;
    }

    public void setDatetimebegin(Date datetimebegin) {
        this.datetimebegin = datetimebegin;
    }

    public Date getDatetimeend() {
        return datetimeend;
    }

    public void setDatetimeend(Date datetimeend) {
        this.datetimeend = datetimeend;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
