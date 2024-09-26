package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.EquipmentAvailability;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EquipmentAvailabilityRepository extends CrudRepository<EquipmentAvailability, Long> {
    @Override
    Optional<EquipmentAvailability> findById(Long id);
}
