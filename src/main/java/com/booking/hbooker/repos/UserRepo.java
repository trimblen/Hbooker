package com.booking.hbooker.repos;

import com.booking.hbooker.entities.Role;
import com.booking.hbooker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
    User deleteById(long id);

    Page<User> findAllByRolesContains(Role roles, Pageable pageable);
    Page<User> findAll(Pageable pageable);

    List<User> findAllByRolesContains(Role roles);
    List<User> findAll();
}
