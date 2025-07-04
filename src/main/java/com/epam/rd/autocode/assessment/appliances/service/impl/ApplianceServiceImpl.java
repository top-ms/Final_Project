package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceMapper applianceMapper;
    private final OrderRowRepository orderRowRepository;

    public ApplianceServiceImpl(ApplianceRepository applianceRepository,
                                ManufacturerRepository manufacturerRepository, ApplianceMapper applianceMapper, ModelMapper modelMapper, OrderService orderService, OrderRowRepository orderRowRepository) {
        this.applianceRepository = applianceRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.applianceMapper = applianceMapper;
        this.orderRowRepository = orderRowRepository;
    }


    @Override
    public Page<ApplianceDTO> getAllManufacturersAsDto(Pageable pageable) {
        System.out.println("getAllManufacturersAsDto: " + pageable);

        return applianceRepository.findAll(pageable)
                .map(applianceMapper::toDTO);
    }

    @Override
    public Optional<ApplianceDTO> getByName(String name) {
        return applianceRepository.findByName(name)
                .map(applianceMapper::toDTO);
    }

    @Transactional
    @Override
    public void deleteApplianceById(Long id) {
        orderRowRepository.deleteOrderRowsByApplianceId(id);
        applianceRepository.deleteById(id);
    }


    @Override
    public void addNewAppliance(ApplianceDTO applianceDTO) {
        System.out.println("Before save: " + applianceDTO.toString() + "");
        System.out.println("Looking for manufacturer name: " + applianceDTO.getManufacturer());

        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByName(applianceDTO.getManufacturer());
        System.out.println("Optional manufacturer: " + optionalManufacturer.toString() + "");
        Manufacturer manufacturer = optionalManufacturer.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manufacturer not found: " + applianceDTO.getManufacturer()));

        System.out.println("Manufacturer: " + manufacturer.toString() + "");

        Appliance appliance = applianceMapper.toEntity(applianceDTO, manufacturer);
        System.out.println("After save: " + appliance.toString() + "");

        applianceRepository.save(appliance);
    }










    @Override
    public List<Appliance> getAllAppliance() {
        return applianceRepository.findAll();
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
