package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.Rental;
import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {
    @Override
    Rental save(Rental rental);
    @Override
    List<Rental> findAll();
    List<Rental> findByStatusIn(List<RentalStatus> statuses);
    @Override
    Optional<Rental> findById(Long id);
    boolean existsRentalByEquipmentAndStatus(Equipment equipment, RentalStatus rentalStatus);
    List<Rental> findByRenterId(Long renterId);
    @Override
    void deleteById(Long id);
}
