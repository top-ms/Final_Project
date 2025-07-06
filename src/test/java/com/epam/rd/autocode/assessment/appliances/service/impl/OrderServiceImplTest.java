package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrdersRepository ordersRepository;
    private OrderRowRepository orderRowRepository;
    private ApplianceRepository applianceRepository;
    private ModelMapper modelMapper;
    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        ordersRepository = mock(OrdersRepository.class);
        orderRowRepository = mock(OrderRowRepository.class);
        applianceRepository = mock(ApplianceRepository.class);
        modelMapper = new ModelMapper();

        service = new OrderServiceImpl(ordersRepository, orderRowRepository, applianceRepository, modelMapper);
    }

    @Test
    void testSaveNewOrderRowById_success() {
        Orders order = new Orders();
        order.setId(1L);

        Appliance appliance = new Appliance();
        appliance.setId(2L);
        appliance.setPrice(BigDecimal.TEN);

        when(ordersRepository.findById(1L)).thenReturn(Optional.of(order));
        when(applianceRepository.findById(2L)).thenReturn(Optional.of(appliance));

        service.saveNewOrderRowById(1L, 2L, 3L);

        verify(orderRowRepository).save(any(OrderRow.class));
    }

    @Test
    void testFindOrderDtoById_success() {
        Orders order = new Orders();
        order.setId(1L);
        order.setApproved(true);
        order.setOrderRowSet(Set.of());

        Client client = new Client();
        client.setName("Ivan");
        client.setEmail("ivan@example.com");

        Employee employee = new Employee();
        employee.setName("Petro");
        employee.setEmail("petro@example.com");

        order.setClient(client);
        order.setEmployee(employee);

        when(ordersRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<ViewOrdersDTO> dto = service.findOrderDtoById(1L);

        assertTrue(dto.isPresent());
        assertEquals("Ivan", dto.get().getClientName());
        assertEquals("Petro", dto.get().getEmployeeName());
    }

    @Test
    void testSetApproved_success() {
        Orders order = new Orders();
        order.setId(1L);
        order.setApproved(false);

        when(ordersRepository.findById(1L)).thenReturn(Optional.of(order));

        service.setApproved(1L, true);

        assertTrue(order.getApproved());
        verify(ordersRepository).save(order);
    }

    @Test
    void testDeleteOrderById() {
        service.deleteOrderById(5L);
        verify(ordersRepository).deleteById(5L);
    }

    @Test
    void testDeleteOrderRowById() {
        service.deleteOrderRowById(10L);
        verify(orderRowRepository).deleteById(10L);
    }

    @Test
    void testGetAllOrders() {
        Orders order1 = new Orders();
        Orders order2 = new Orders();
        when(ordersRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Orders> allOrders = service.getAllOrders();
        assertEquals(2, allOrders.size());
    }

    @Test
    void testGetAllOrdersAsDto() {
        Orders order = new Orders();
        order.setId(1L);
        order.setApproved(true);
        order.setOrderRowSet(Set.of());

        Client client = new Client();
        client.setName("Ivan");
        client.setEmail("ivan@example.com");

        Employee employee = new Employee();
        employee.setName("Petro");
        employee.setEmail("petro@example.com");

        order.setClient(client);
        order.setEmployee(employee);

        Page<Orders> page = new PageImpl<>(List.of(order));
        when(ordersRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ViewOrdersDTO> result = service.getAllOrdersAsDto(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Ivan", result.getContent().get(0).getClientName());
    }
}
