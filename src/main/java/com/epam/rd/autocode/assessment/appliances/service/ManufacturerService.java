package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> findAll();
    Manufacturer findById(Long id);
    Manufacturer save(Manufacturer manufacturer);
    void deleteById(Long id);
}
