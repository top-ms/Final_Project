package com.epam.rd.autocode.assessment.appliances.controler.clientRole;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersControllerForClient.class)
class OrdersControllerForClientTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ApplianceService applianceService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    // Тестові дані
    private Client createTestClient() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("client@test.com");
        client.setName("Test Client");
        return client;
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test Employee");
        return employee;
    }

    private Orders createTestOrder() {
        Orders order = new Orders();
        order.setId(1L);
        order.setClient(createTestClient());
        order.setEmployee(createTestEmployee());
        order.setApproved(false);
        order.setOrderRowSet(new HashSet<>());
        return order;
    }

    private ViewOrdersDTO createTestOrderDTO() {
        ViewOrdersDTO dto = new ViewOrdersDTO();
        dto.setId(1L);
        dto.setClientName("Test Client");
        dto.setEmployeeName("Test Employee");
        dto.setApproved(false);
        return dto;
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testListOfOrders_Success() throws Exception {
        // Given
        Client client = createTestClient();
        ViewOrdersDTO orderDTO = createTestOrderDTO();
        Page<ViewOrdersDTO> ordersPage = new PageImpl<>(List.of(orderDTO));

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.getOrdersByClientId(eq(1L), any(Pageable.class))).thenReturn(ordersPage);
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        // When & Then
        mockMvc.perform(get("/client/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/orders"))
                .andExpect(model().attributeExists("ordersPage"))
                .andExpect(model().attributeExists("employees"));
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testListOfOrders_WithFilters() throws Exception {
        // Given
        Client client = createTestClient();
        ViewOrdersDTO orderDTO = createTestOrderDTO();
        Page<ViewOrdersDTO> ordersPage = new PageImpl<>(List.of(orderDTO));

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.getOrdersByClientIdAndEmployeeIdAndApproved(eq(1L), eq(1L), eq(true), any(Pageable.class)))
                .thenReturn(ordersPage);
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        // When & Then
        mockMvc.perform(get("/client/orders")
                        .param("employeeId", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/orders"))
                .andExpect(model().attributeExists("ordersPage"));
    }


    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testSearchOrders_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        ViewOrdersDTO orderDTO = createTestOrderDTO();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);
        when(orderService.findOrderDtoById(1L)).thenReturn(Optional.of(orderDTO));
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        // When & Then
        mockMvc.perform(get("/client/orders/search")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/orders"))
                .andExpect(model().attributeExists("ordersPage"));
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testSearchOrders_NotOwner() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        Client otherClient = new Client();
        otherClient.setId(2L);
        order.setClient(otherClient);

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        // When & Then
        mockMvc.perform(get("/client/orders/search")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/orders"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testDeleteOrder_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders?page=0"));

        verify(orderService).deleteOrderById(1L);
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testDeleteOrder_NotOwner() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        Client otherClient = new Client();
        otherClient.setId(2L);
        order.setClient(otherClient);

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders?page=0"));

        verify(orderService, never()).deleteOrderById(1L);
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testEditOrder_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/editOrder"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("rows"));
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testEditOrder_NotOwner() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        Client otherClient = new Client();
        otherClient.setId(2L);
        order.setClient(otherClient);

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders"));
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testChoiceAppliance_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Test Appliance");

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);
        when(applianceService.getAllAppliances()).thenReturn(List.of(appliance));

        // When & Then
        mockMvc.perform(get("/client/orders/1/choice-appliance"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/order/choiceAppliance"))
                .andExpect(model().attributeExists("appliances"))
                .andExpect(model().attributeExists("ordersId"));
    }


    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testApproveOrder_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/approved"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders?page=0"));

        verify(orderService).setApproved(1L, true);
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testUnapproveOrder_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/unapproved"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders?page=0"));

        verify(orderService).setApproved(1L, false);
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testDeleteOrderRow_Success() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/edit/row/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders/1/edit"));

        verify(orderService).deleteOrderRowById(1L);
    }

    @Test
    @WithMockUser(username = "client@test.com", authorities = {"ROLE_CLIENT"})
    void testDeleteOrderRow_NotOwner() throws Exception {
        // Given
        Client client = createTestClient();
        Orders order = createTestOrder();
        Client otherClient = new Client();
        otherClient.setId(2L);
        order.setClient(otherClient);

        when(clientService.findClientEntityByEmail("client@test.com")).thenReturn(client);
        when(orderService.findById(1L)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/client/orders/1/edit/row/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/orders"));

        verify(orderService, never()).deleteOrderRowById(1L);
    }
}