package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.AddNewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.ViewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {

    Page<ViewManufacturerAsDTO> getAllManufacturersAsDto(Pageable pageable);

    Optional<ViewManufacturerAsDTO> getByName(String name);

    boolean existsByName(String name);

    List<Manufacturer> getAllManufacturers();

    void saveNewManufacturer(AddNewManufacturerAsDTO addNewManufacturerAsDTO);

    void deleteManufacturerById(Long id);
}
