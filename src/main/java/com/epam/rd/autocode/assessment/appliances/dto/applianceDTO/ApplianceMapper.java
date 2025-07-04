package com.epam.rd.autocode.assessment.appliances.dto.applianceDTO;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.springframework.stereotype.Component;

@Component
public class ApplianceMapper {

    public ApplianceDTO toDTO(Appliance appliance) {
        ApplianceDTO dto = new ApplianceDTO();
        dto.setId(appliance.getId());
        dto.setName(appliance.getName());
        dto.setCategory(appliance.getCategory().name()); // enum → string
        dto.setModel(appliance.getModel());

        if (appliance.getManufacturer() != null) {
            dto.setManufacturer(appliance.getManufacturer().getName()); // only name
        } else {
            dto.setManufacturer("N/A");
        }

        dto.setPowerType(appliance.getPowerType().name()); // enum → string
        dto.setCharacteristic(appliance.getCharacteristic());
        dto.setDescription(appliance.getDescription());
        dto.setPower(appliance.getPower());
        dto.setPrice(appliance.getPrice());

        return dto;
    }

    public Appliance toEntity(ApplianceDTO dto, Manufacturer manufacturer) {
        Appliance appliance = new Appliance();
        appliance.setId(dto.getId());
        appliance.setName(dto.getName());
        appliance.setCategory(Category.valueOf(dto.getCategory()));
        appliance.setModel(dto.getModel());
        appliance.setManufacturer(manufacturer);
        appliance.setPowerType(PowerType.valueOf(dto.getPowerType()));
        appliance.setCharacteristic(dto.getCharacteristic());
        appliance.setDescription(dto.getDescription());
        appliance.setPower(dto.getPower());
        appliance.setPrice(dto.getPrice());
        return appliance;
    }

}
