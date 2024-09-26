package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.controller.exception.*;
import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.repository.EquipmentPriceRepository;
import com.kodilla.outdoor.equiprent.repository.EquipmentRepository;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
import com.kodilla.outdoor.equiprent.repository.RenterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentPriceRepository equipmentPriceRepository;
    private final RenterRepository renterRepository;

    public Rental createRental(Long equipmentId, Long renterId, Long rentalTierId, Integer rentalTierQuantity) throws EquipmentNotFoundException, EquipmentNotAvailableException, TierNotAvailableException, RenterNotFoundException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException(equipmentId));

        if (!equipment.getEquipmentAvailability().isAvailable() || equipment.getEquipmentAvailability().getCurrentQuantity() <= 0) {
            throw new EquipmentNotAvailableException(equipmentId);
        }

        EquipmentPrice equipmentPrice = equipmentPriceRepository.findById(rentalTierId)
                .orElseThrow(() -> new TierNotAvailableException(rentalTierId));

        LocalDateTime rentalStart = LocalDateTime.now();
        LocalDateTime rentalEnd = switch (equipmentPrice.getTier()) {
            case HOUR -> rentalStart.plusHours(rentalTierQuantity);
            case DAY -> rentalStart.plusDays(rentalTierQuantity);
            case WEEK -> rentalStart.plusWeeks(rentalTierQuantity);
        };

        Renter renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new RenterNotFoundException(renterId));

        Rental rental = Rental.builder()
                .equipment(equipment)
                .renter(renter)
                .rentalStart(rentalStart)
                .rentalEnd(rentalEnd)
                .status(RentalStatus.ACTIVE)
                .totalPrice(calculateRentalTotalPrice(equipmentPrice, rentalTierQuantity))
                .creationDate(LocalDateTime.now())
                .build();

        equipment.getEquipmentAvailability().setCurrentQuantity(equipment.getEquipmentAvailability().getCurrentQuantity() - 1);
        equipmentRepository.save(equipment);

        return rentalRepository.save(rental);
    }

    private BigDecimal calculateRentalTotalPrice(EquipmentPrice equipmentPrice, Integer rentalTierQuantity) {
        return equipmentPrice.getPrice().multiply(new BigDecimal(rentalTierQuantity));
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalByStatuses(List<RentalStatus> statuses) {
        if (statuses.isEmpty()) {
            return getAllRentals();
        } else {
            return rentalRepository.findByStatusIn(statuses);
        }
    }

    public Rental updateRentalStatus(Long rentalId, RentalStatus rentalStatus) throws RentalNotFoundException, InvalidRentalStatusChangeException {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if (!rental.getStatus().equals(RentalStatus.ACTIVE)) {
            throw new InvalidRentalStatusChangeException(rentalStatus, rental.getStatus());
        }

        if (!rentalStatus.equals(RentalStatus.ACTIVE)) {
            rental.setStatus(rentalStatus);
            rental.getEquipment().getEquipmentAvailability().setCurrentQuantity(rental.getEquipment().getEquipmentAvailability().getCurrentQuantity() + 1);
        }

        return rentalRepository.save(rental);
    }

    public List<Rental> getRentalsByRenterId(Long renterId) {
        return rentalRepository.findByRenterId(renterId);
    }
}
