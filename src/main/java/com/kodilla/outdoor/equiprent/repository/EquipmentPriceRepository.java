package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentPrice;
import com.kodilla.outdoor.equiprent.domain.Tier;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EquipmentPriceRepository extends CrudRepository<EquipmentPrice, Long> {
    @Override
    Optional<EquipmentPrice> findById(Long id);
    Optional<EquipmentPrice> findByEquipmentAndTier(Equipment equipment, Tier tier);
}
