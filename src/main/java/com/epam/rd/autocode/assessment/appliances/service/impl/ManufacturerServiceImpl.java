package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.AddNewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.ViewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceRepository applianceRepository;
    private final OrderRowRepository orderRowRepository;
    private final ModelMapper modelMapper;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ApplianceRepository applianceRepository, OrderRowRepository orderRowRepository, ModelMapper modelMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.applianceRepository = applianceRepository;
        this.orderRowRepository = orderRowRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<ViewManufacturerAsDTO> getByName(String name) {
        return manufacturerRepository.findByName(name)
                .map(manufacturer -> modelMapper.map(manufacturer, ViewManufacturerAsDTO.class));
    }

    @Override
    public Page<ViewManufacturerAsDTO> getAllManufacturersAsDto(Pageable pageable) {
        return manufacturerRepository.findAll(pageable)
                .map(manufacturer -> modelMapper.map(manufacturer, ViewManufacturerAsDTO.class));
    }

    @Override
    public boolean existsByName(String name) {
        return manufacturerRepository.findByName(name).isPresent();
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public void saveNewManufacturer(AddNewManufacturerAsDTO addNewManufacturerAsDTO) {
        Manufacturer manufacturer = modelMapper.map(addNewManufacturerAsDTO, Manufacturer.class);
        manufacturerRepository.save(manufacturer);
    }


    @Transactional
    @Override
    public void deleteManufacturerById(Long manufacturerId) {
        List<Appliance> appliances = applianceRepository.findAllByManufacturerId(manufacturerId);

        for (Appliance appliance : appliances) {
            orderRowRepository.deleteAllByApplianceId(appliance.getId());
        }


        applianceRepository.deleteAll(appliances);
        manufacturerRepository.deleteById(manufacturerId);
    }
}