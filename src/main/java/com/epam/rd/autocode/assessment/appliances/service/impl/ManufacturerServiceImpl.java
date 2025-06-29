package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceRepository applianceRepository;
    private final OrderRowRepository orderRowRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ApplianceRepository applianceRepository, OrderRowRepository orderRowRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.applianceRepository = applianceRepository;
        this.orderRowRepository = orderRowRepository;
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public void saveNewManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }

    @Transactional
    @Override
    public void deleteManufacturerById(Long manufacturerId) {
        List<Appliance> appliances = applianceRepository.findAllByManufacturerId(manufacturerId);

        for (Appliance appliance : appliances) {
            // Видалити всі orderRows для цього appliance
            orderRowRepository.deleteAllByApplianceId(appliance.getId());
        }

        // Тепер видалити всі appliances цього виробника
        applianceRepository.deleteAll(appliances);

        // І вже потім — видалити самого виробника
        manufacturerRepository.deleteById(manufacturerId);
    }
}