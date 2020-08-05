package com.booking.hbooker.repos;

import com.booking.hbooker.entities.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface ApartmentRepo extends PagingAndSortingRepository<Apartment, Long> {
    Apartment findByAprtname(String aprtname);
    Apartment deleteById(long id);

    Page<Apartment> findAllByDeletionMark(boolean deletionMark, Pageable pageable);
    Page<Apartment> findAll(Pageable pageable);

    List<Apartment> findByIdNotInAndDeletionMarkAndHotel_Hotelcat_Id(List<Long> id, boolean deletionMark,long hotcat_id);
    List<Apartment> findAll();
    List<Apartment> findAllByDeletionMark(boolean deletionMark);
}
