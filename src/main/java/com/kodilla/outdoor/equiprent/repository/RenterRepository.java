package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Renter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RenterRepository extends CrudRepository<Renter, Long> {
    @Override
    Optional<Renter> findById(Long id);
    @Override
    Renter save(Renter renter);
    boolean existsByEmail(String email);
    @Override
    List<Renter> findAll();
}
