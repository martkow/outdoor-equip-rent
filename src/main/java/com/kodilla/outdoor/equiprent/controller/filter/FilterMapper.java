package com.kodilla.outdoor.equiprent.controller.filter;

import com.kodilla.outdoor.equiprent.controller.exception.CategoryNotFoundException;
import com.kodilla.outdoor.equiprent.controller.exception.RentalStatusNotFoundException;
import com.kodilla.outdoor.equiprent.domain.EquipmentCategory;
import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FilterMapper {
    public List<EquipmentCategory> mapStringToEquipmentCategoryList(Optional<String> category) throws CategoryNotFoundException {
        List<EquipmentCategory> categories = new ArrayList<>();

        if (category.isPresent()) {
            String[] categoryArray = category.get().split(",");

            for (String cat : categoryArray) {
                categories.add(mapToCategoryOrThrow(cat));
            }
        }

        return categories;
    }

    public EquipmentCategory mapToCategoryOrThrow(String category) throws CategoryNotFoundException {
        try {
            return EquipmentCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotFoundException(category);
        }
    }

    public List<RentalStatus> mapStringToRentalStatusList(Optional<String> status) throws RentalStatusNotFoundException {
        List<RentalStatus> statuses = new ArrayList<>();

        if (status.isPresent()) {
            String[] statusArray = status.get().split(",");

            for (String sta : statusArray) {
                String upperCaseStatus = sta.toUpperCase();
                statuses.add(mapToStatusOrThrow(upperCaseStatus));
            }
        }

        return statuses;
    }

    public RentalStatus mapToStatusOrThrow(String status) throws RentalStatusNotFoundException {
        try {
            return RentalStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new RentalStatusNotFoundException(status);
        }
    }
}
