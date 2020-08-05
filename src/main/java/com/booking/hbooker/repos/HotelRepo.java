package com.booking.hbooker.repos;

import com.booking.hbooker.entities.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HotelRepo extends PagingAndSortingRepository<Hotel, Long> {
    Hotel findByHotelname(String hotelname);
    Hotel findById(long id);
    Hotel deleteById(long id);

    Page<Hotel> findAllByDeletionMark(boolean deletionMark, Pageable pageable);
    Page<Hotel> findAll(Pageable pageable);

    List<Hotel> findAllByDeletionMark(boolean deletionMark);
    List<Hotel> findAll();
}
