
package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplianceService {

    Page<ApplianceDTO> getAllManufacturersAsDto(Pageable pageable);

    Optional<ApplianceDTO> getByName(String name);

    void deleteApplianceById(Long id);

    List<Appliance> getAllAppliance();

    void addNewAppliance(ApplianceDTO applianceDTO);







    List<Manufacturer> getAllManufacturers();

    List<Appliance> getAllAppliances();

    Appliance findById(Long id);

}