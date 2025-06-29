package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> getAllManufacturers();
    void saveNewManufacturer(Manufacturer manufacturer);
    void deleteManufacturerById(Long id);
}
