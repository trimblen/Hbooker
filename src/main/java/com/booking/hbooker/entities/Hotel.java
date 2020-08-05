package com.booking.hbooker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "hotels_Sequence")
    @SequenceGenerator(name = "hotel_Sequence", sequenceName = "HOTELS_SEQ")
    private Long id;

    @NotBlank(message = "Please enter hotelname.")
    @Column(name = "hotelname")
    private String hotelname;
    //private String hotelAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotelcat_id")
    HotelCat hotelcat;

    private String hDescription;

    boolean deletionMark;

    public Hotel(){

    }

    public boolean isDeletionMark() {
        return deletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    public String gethDescription() {
        return hDescription != null ? hDescription  : "<none>";
    }

    public void sethDescription(String hDescription) {
        this.hDescription = hDescription;
    }

    public HotelCat getHotelcat() {
        return hotelcat;
    }

    public void setHotelcat(HotelCat hotelcat) {
        this.hotelcat = hotelcat;
    }

    public String getHotelname() {
        return hotelname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }
}
