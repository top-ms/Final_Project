package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplianceServiceImplTest {

    private ApplianceRepository applianceRepository;
    private ManufacturerRepository manufacturerRepository;
    private OrderRowRepository orderRowRepository;
    private ViewApplianceMapper viewApplianceMapper;

    private ApplianceServiceImpl service;

    @BeforeEach
    void setUp() {
        applianceRepository = mock(ApplianceRepository.class);
        manufacturerRepository = mock(ManufacturerRepository.class);
        orderRowRepository = mock(OrderRowRepository.class);
        viewApplianceMapper = mock(ViewApplianceMapper.class);

        service = new ApplianceServiceImpl(
                applianceRepository,
                manufacturerRepository,
                viewApplianceMapper,
                null,
                null,
                null,
                orderRowRepository
        );
    }

    @Test
    void shouldReturnPageOfApplianceDTOs() {
        Appliance appliance = new Appliance();
        ViewApplianceDTO dto = new ViewApplianceDTO();

        when(applianceRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(appliance)));

        when(viewApplianceMapper.toDTO(appliance)).thenReturn(dto);

        Page<ViewApplianceDTO> result = service.getAllManufacturersAsDto(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(viewApplianceMapper).toDTO(appliance);
    }

    @Test
    void shouldAddNewApplianceIfManufacturerExists() {
        ViewApplianceDTO dto = new ViewApplianceDTO();
        dto.setManufacturer("Samsung");
        dto.setCategory("WASHING");
        dto.setPowerType("ELECTRIC");
        dto.setName("Model X");
        dto.setModel("X-123");
        dto.setCharacteristic("specs");
        dto.setDescription("desc");
        dto.setPower(1500);
        dto.setPrice(BigDecimal.valueOf(499.99));

        Manufacturer manufacturer = new Manufacturer();
        Appliance appliance = new Appliance();

        when(manufacturerRepository.findByName("Samsung")).thenReturn(Optional.of(manufacturer));
        when(viewApplianceMapper.toEntity(dto, manufacturer)).thenReturn(appliance);

        service.addNewAppliance(dto);

        verify(applianceRepository).save(appliance);
    }

    @Test
    void shouldThrowExceptionWhenManufacturerNotFound() {
        ViewApplianceDTO dto = new ViewApplianceDTO();
        dto.setManufacturer("NonExistent");

        when(manufacturerRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.addNewAppliance(dto));
    }

    @Test
    void shouldDeleteApplianceAndItsOrdersById() {
        Long id = 42L;

        service.deleteApplianceById(id);

        verify(orderRowRepository).deleteOrderRowsByApplianceId(id);
        verify(applianceRepository).deleteById(id);
    }
}
