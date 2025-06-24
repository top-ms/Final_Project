package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;

    public ApplianceServiceImpl(ApplianceRepository applianceRepository,
                                ManufacturerRepository manufacturerRepository) {
        this.applianceRepository = applianceRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Appliance> getAllAppliance() {
        return applianceRepository.findAll();
    }

    @Override
    public void addNewAppliance(Appliance appliance) {
        this.applianceRepository.save(appliance);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public List<Appliance> getAllAppliances() {
        return applianceRepository.findAll();
    }

    @Override
    public Appliance findById(Long id) {
        return applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appliance not find with id = " + id));
    }

}