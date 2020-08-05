package com.booking.hbooker.repos;

import com.booking.hbooker.entities.HotelCat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HotelCatRepo extends PagingAndSortingRepository<HotelCat, Long> {
    HotelCat findByCatname(String catname);
    HotelCat findById(long Id);
    HotelCat deleteById(long Id);

    List<HotelCat> findAll();
    List<HotelCat> findAllByDeletionMark(boolean deletionMark);

    Page<HotelCat> findAll(Pageable pageable);
    Page<HotelCat> findAllByDeletionMark(boolean deletionMark, Pageable pageable);
}
