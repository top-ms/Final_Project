
package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;

import java.util.List;

public interface ApplianceService {
    Appliance create(Appliance appliance);
    Appliance update(Long id, Appliance appliance);
    void delete(Long id);
    Appliance findById(Long id);
    List<Appliance> findAll();
}