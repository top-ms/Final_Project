
package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.EditApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplianceService {

    Page<ViewApplianceDTO> getAllManufacturersAsDto(Pageable pageable);

    Optional<ViewApplianceDTO> getByName(String name);

    void deleteApplianceById(Long id);

    List<Appliance> getAllAppliance();

    void addNewAppliance(ViewApplianceDTO viewApplianceDTO);

    Optional<ViewApplianceDTO> getApplianceById(Long id);

    void updateAppliance(Long id, ViewApplianceDTO dto);



    List<Manufacturer> getAllManufacturers();

    List<Appliance> getAllAppliances();

}