package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.AddNewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.ViewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManufacturerServiceImplTest {

    private ManufacturerRepository manufacturerRepository;
    private ApplianceRepository applianceRepository;
    private OrderRowRepository orderRowRepository;
    private ModelMapper modelMapper;

    private ManufacturerServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        manufacturerRepository = mock(ManufacturerRepository.class);
        applianceRepository = mock(ApplianceRepository.class);
        orderRowRepository = mock(OrderRowRepository.class);
        modelMapper = mock(ModelMapper.class);

        Constructor<ManufacturerServiceImpl> constructor = ManufacturerServiceImpl.class.getConstructor(
                ManufacturerRepository.class,
                ApplianceRepository.class,
                OrderRowRepository.class,
                ModelMapper.class
        );
        service = constructor.newInstance(manufacturerRepository, applianceRepository, orderRowRepository, modelMapper);
    }

    @Test
    void testGetByName() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("LG");

        ViewManufacturerAsDTO dto = new ViewManufacturerAsDTO(1L, "LG");

        when(manufacturerRepository.findByName("LG")).thenReturn(Optional.of(manufacturer));
        when(modelMapper.map(manufacturer, ViewManufacturerAsDTO.class)).thenReturn(dto);

        Optional<ViewManufacturerAsDTO> result = service.getByName("LG");

        assertTrue(result.isPresent());
        assertEquals("LG", result.get().getName());
    }

    @Test
    void testExistsByName() {
        when(manufacturerRepository.findByName("Samsung")).thenReturn(Optional.of(new Manufacturer()));

        boolean exists = service.existsByName("Samsung");

        assertTrue(exists);
    }

    @Test
    void testGetAllManufacturersAsDto() {
        Manufacturer m = new Manufacturer(1L, "Bosch");
        Page<Manufacturer> manufacturerPage = new PageImpl<>(List.of(m));
        ViewManufacturerAsDTO dto = new ViewManufacturerAsDTO(1L, "Bosch");

        when(manufacturerRepository.findAll(PageRequest.of(0, 10))).thenReturn(manufacturerPage);
        when(modelMapper.map(m, ViewManufacturerAsDTO.class)).thenReturn(dto);

        Page<ViewManufacturerAsDTO> result = service.getAllManufacturersAsDto(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Bosch", result.getContent().get(0).getName());
    }

    @Test
    void testSaveNewManufacturer() {
        AddNewManufacturerAsDTO dto = new AddNewManufacturerAsDTO("Siemens");
        Manufacturer m = new Manufacturer();
        m.setName("Siemens");

        when(modelMapper.map(dto, Manufacturer.class)).thenReturn(m);

        service.saveNewManufacturer(dto);

        verify(manufacturerRepository).save(m);
    }

    @Test
    void testDeleteManufacturerById() {
        Long id = 10L;
        Appliance appliance1 = new Appliance();
        appliance1.setId(101L);
        Appliance appliance2 = new Appliance();
        appliance2.setId(102L);

        when(applianceRepository.findAllByManufacturerId(id)).thenReturn(List.of(appliance1, appliance2));

        service.deleteManufacturerById(id);

        verify(orderRowRepository).deleteAllByApplianceId(101L);
        verify(orderRowRepository).deleteAllByApplianceId(102L);
        verify(applianceRepository).deleteAll(List.of(appliance1, appliance2));
        verify(manufacturerRepository).deleteById(id);
    }
}
