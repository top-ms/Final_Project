package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.EditApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
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
    private final ModelMapper modelMapper;
    private final ViewApplianceMapper viewApplianceMapper;
    private final OrderRowRepository orderRowRepository;

    public ApplianceServiceImpl(ApplianceRepository applianceRepository,
                                ManufacturerRepository manufacturerRepository, ViewApplianceMapper viewApplianceMapper, ModelMapper modelMapper, OrderService orderService, ModelMapper modelMapper1, OrderRowRepository orderRowRepository) {
        this.applianceRepository = applianceRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.viewApplianceMapper = viewApplianceMapper;
        this.modelMapper = modelMapper1;
        this.orderRowRepository = orderRowRepository;
    }

    @Override
    public Page<ViewApplianceDTO> getAllManufacturersAsDto(Pageable pageable) {
        System.out.println("getAllManufacturersAsDto: " + pageable);

        return applianceRepository.findAll(pageable)
                .map(viewApplianceMapper::toDTO);
    }

    @Override
    public Optional<ViewApplianceDTO> getByName(String name) {
        return applianceRepository.findByName(name)
                .map(viewApplianceMapper::toDTO);
    }

    @Transactional
    @Override
    public void deleteApplianceById(Long id) {
        orderRowRepository.deleteOrderRowsByApplianceId(id);
        applianceRepository.deleteById(id);
    }

    @Override
    public void addNewAppliance(ViewApplianceDTO viewApplianceDTO) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByName(viewApplianceDTO.getManufacturer());
        Manufacturer manufacturer = optionalManufacturer.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manufacturer not found: " + viewApplianceDTO.getManufacturer()));
        Appliance appliance = viewApplianceMapper.toEntity(viewApplianceDTO, manufacturer);
        applianceRepository.save(appliance);
    }

    @Override
    public Optional<ViewApplianceDTO> getApplianceById(Long id) {
        return applianceRepository.findById(id)
                .map(viewApplianceMapper::toDTO);
    }

    public void updateAppliance(Long id, ViewApplianceDTO dto) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appliance not found with id: " + id));
        appliance.setName(dto.getName());
        appliance.setModel(dto.getModel());
        appliance.setCategory(Category.valueOf(dto.getCategory()));
        appliance.setPowerType(PowerType.valueOf(dto.getPowerType()));
        appliance.setCharacteristic(dto.getCharacteristic());
        appliance.setDescription(dto.getDescription());
        appliance.setPower(dto.getPower());
        appliance.setPrice(dto.getPrice());
        Manufacturer manufacturer = manufacturerRepository.findByName(dto.getManufacturer())
                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
        appliance.setManufacturer(manufacturer);
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
}
