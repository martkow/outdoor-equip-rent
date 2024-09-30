package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.exception.ActiveEquipmentRentalException;
import com.kodilla.outdoor.equiprent.exception.EquipmentNotFoundException;
import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import com.kodilla.outdoor.equiprent.repository.EquipmentRepository;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final RentalRepository rentalRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long equipmentId) throws EquipmentNotFoundException {
        return equipmentRepository.findById(equipmentId).orElseThrow(() -> new EquipmentNotFoundException(equipmentId));
    }

    public List<Equipment> getEquipmentByCategories(List<EquipmentCategory> categories) {
        if (categories.isEmpty()) {
            return getAllEquipment();
        } else {
            return equipmentRepository.findByCategoryIn(categories);
        }
    }

    public List<EquipmentCategory> getAllCategories() {
        return List.of(EquipmentCategory.values());
    }

    public Equipment addEquipment(Equipment equipment) {
        equipment.setCreationDate(LocalDateTime.now());
        equipment.getEquipmentAvailability().setAvailable(true);
        equipment.getEquipmentAvailability().setCreationDate(LocalDateTime.now());
        equipment.getPrices().forEach(ep -> ep.setCreationDate(LocalDateTime.now()));

        return equipmentRepository.save(equipment);
    }

    public void updateEquipmentAvailability(Long equipmentId) throws EquipmentNotFoundException, ActiveEquipmentRentalException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException(equipmentId));

        if (rentalRepository.existsRentalByEquipmentAndStatus(equipment, RentalStatus.ACTIVE)) {
            throw new ActiveEquipmentRentalException(equipmentId);
        }

        equipment.getEquipmentAvailability().setAvailable(false);

        equipmentRepository.save(equipment);
    }
}
