package com.booking.hbooker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "apartments_Sequence")
    @SequenceGenerator(name = "apartments_Sequence", sequenceName = "APARTMENTS_SEQ")
    private Long id;

    @NotBlank(message = "Please enter apartment name.")
    @Column(name = "aprtname")
    private String aprtname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    private String aptDescription;

    boolean deletionMark;

    public Apartment(){

    }

    public boolean isDeletionMark() {
        return deletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    public String getAptDescription() {
        return aptDescription != null ? aptDescription  : "<none>";
    }

    public void setAptDescription(String aptDescription) {
        this.aptDescription = aptDescription;
    }

    public String getAprtname() {
        return aprtname;
    }

    public void setAprtname(String aprtname) {
        this.aprtname = aprtname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

}
