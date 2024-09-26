package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    @Override
    List<Equipment> findAll();
    @Override
    Equipment save(Equipment equipment);
    @Override
    void deleteById(Long id);
    List<Equipment> findByCategoryIn(List<EquipmentCategory> categories);
    @Override
    Optional<Equipment> findById(Long id);
}
