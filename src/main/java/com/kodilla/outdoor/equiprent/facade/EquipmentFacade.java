package com.kodilla.outdoor.equiprent.facade;

import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.dto.CreateEquipmentDto;
import com.kodilla.outdoor.equiprent.dto.EquipmentDto;
import com.kodilla.outdoor.equiprent.exception.ActiveEquipmentRentalException;
import com.kodilla.outdoor.equiprent.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.exception.EquipmentNotFoundException;
import com.kodilla.outdoor.equiprent.mapper.EquipmentMapper;
import com.kodilla.outdoor.equiprent.mapper.FilterMapper;
import com.kodilla.outdoor.equiprent.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentFacade {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;
    private final FilterMapper filterMapper;

    public List<EquipmentDto> getAllAvailableEquipment(Optional<String> category) throws CategoryNotFoundException {
        return equipmentMapper.mapEquipmentListToEquipmentDtoList(
                equipmentService.getEquipmentByCategories(
                        filterMapper.mapStringToEquipmentCategoryList(category)
                )
        );
    }

    public List<EquipmentCategory> getAllCategories() {
        return equipmentService.getAllCategories();
    }

    public EquipmentDto createEquipmentInCategory(String category, CreateEquipmentDto createEquipmentDto) throws CategoryNotFoundException {
        return equipmentMapper.mapEquipmentToEquipmentDto(
                equipmentService.addEquipment(
                        equipmentMapper.mapCreateEquipmentDtoToEquipment(filterMapper.mapToCategoryOrThrow(category), createEquipmentDto)
                )
        );
    }

    public void deleteEquipmentFromCategory(String category, Long equipmentId) throws CategoryNotFoundException, EquipmentNotFoundException, ActiveEquipmentRentalException {
        filterMapper.mapToCategoryOrThrow(category);

        equipmentService.updateEquipmentAvailability(equipmentId);
    }
}
