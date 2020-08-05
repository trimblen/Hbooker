package com.booking.hbooker.repos;

import com.booking.hbooker.entities.Apartment;
import com.booking.hbooker.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface BookingRepo extends PagingAndSortingRepository<Booking, Long> {
    @Query(value = "SELECT b.apartment_id FROM bookings b WHERE b.datetimeend >= :datetimebegin AND b.datetimebegin<=:datetimeend AND b.hotcat_id =:hotcatId", nativeQuery = true)
    List<Long> findByPrerequisites(@Param("datetimebegin") Date datetimebegin, @Param("datetimeend") Date datetimeend, @Param("hotcatId") Long hotcatId);

    Page<Booking> findAllByAuthorId(long authorId, Pageable pageable);
    Page<Booking> findAll(Pageable pageable);

    List<Booking> findAll();
    List<Booking> findAllByAuthorId(long authorId);

    Booking deleteById(long id);
}
