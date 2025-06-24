
package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

import java.util.List;

public interface ApplianceService {
    List<Appliance> getAllAppliance();
    void addNewAppliance(Appliance appliance);
    List<Manufacturer> getAllManufacturers();
    List<Appliance> getAllAppliances();
    Appliance findById(Long id);
}