package com.booking.hbooker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "hotcats")
public class HotelCat {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "hotelcat_Sequence")
    @SequenceGenerator(name = "hotelcat_Sequence", sequenceName = "HOTELCAT_SEQ")
    private Long id;

    @NotBlank(message = "Please enter catname.")
    @Column(name = "catname")
    private String catname;

    boolean deletionMark;

    public HotelCat(){

    }

    public boolean isDeletionMark() {
        return deletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }
}
