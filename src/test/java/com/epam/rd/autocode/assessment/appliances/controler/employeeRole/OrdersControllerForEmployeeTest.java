package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.CreateOrderDTO;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersControllerForEmployee.class)
class OrdersControllerForEmployeeTest {

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
        employee.setEmail("employee@test.com");
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

    private ViewClientsByAdminDTO createTestClientDTO() {
        ViewClientsByAdminDTO dto = new ViewClientsByAdminDTO();
        dto.setId(1L);
        dto.setEmail("client@test.com");
        dto.setName("Test Client");
        return dto;
    }

    private CreateOrderDTO createTestCreateOrderDTO() {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setClientEmail("client@test.com");
        return dto;
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testShowNewOrderForm() throws Exception {
        mockMvc.perform(get("/employee/orders/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/newOrder"))
                .andExpect(model().attributeExists("createOrderDTO"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchClient_Success() throws Exception {
        ViewClientsByAdminDTO clientDTO = createTestClientDTO();
        when(clientService.findByEmail("client@test.com")).thenReturn(Optional.of(clientDTO));

        mockMvc.perform(post("/employee/orders/search-client")
                        .with(csrf())
                        .param("clientEmail", "client@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/newOrder"))
                .andExpect(model().attributeExists("createOrderDTO"))
                .andExpect(model().attributeExists("foundClient"))
                .andExpect(model().attribute("clientFound", true));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchClient_NotFound() throws Exception {
        when(clientService.findByEmail("client@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/employee/orders/search-client")
                        .with(csrf())
                        .param("clientEmail", "client@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/newOrder"))
                .andExpect(model().attributeExists("createOrderDTO"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("clientFound", false));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testListOfOrders_Success() throws Exception {
        ViewOrdersDTO orderDTO = createTestOrderDTO();
        Page<ViewOrdersDTO> ordersPage = new PageImpl<>(List.of(orderDTO));

        when(orderService.getAllOrdersAsDto(any(Pageable.class))).thenReturn(ordersPage);
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));
        when(clientService.getAllClients()).thenReturn(List.of(createTestClient()));

        mockMvc.perform(get("/employee/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/orders"))
                .andExpect(model().attributeExists("ordersPage"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testListOfOrders_WithFilters() throws Exception {
        ViewOrdersDTO orderDTO = createTestOrderDTO();
        Page<ViewOrdersDTO> ordersPage = new PageImpl<>(List.of(orderDTO));

        when(orderService.getOrdersByEmployeeIdAndApproved(eq(1L), eq(true), any(Pageable.class)))
                .thenReturn(ordersPage);
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));
        when(clientService.getAllClients()).thenReturn(List.of(createTestClient()));

        mockMvc.perform(get("/employee/orders")
                        .param("employeeId", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/orders"))
                .andExpect(model().attributeExists("ordersPage"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchOrders_Success() throws Exception {
        ViewOrdersDTO orderDTO = createTestOrderDTO();
        when(orderService.findOrderDtoById(1L)).thenReturn(Optional.of(orderDTO));
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        mockMvc.perform(get("/employee/orders/search")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/orders"))
                .andExpect(model().attributeExists("ordersPage"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchOrders_NotFound() throws Exception {
        when(orderService.findOrderDtoById(1L)).thenReturn(Optional.empty());
        when(employeeService.getAllEmployees()).thenReturn(List.of(createTestEmployee()));

        mockMvc.perform(get("/employee/orders/search")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/orders"))
                .andExpect(model().attributeExists("notFound"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testDeleteOrder_Success() throws Exception {
        mockMvc.perform(get("/employee/orders/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/orders?page=0"));

        verify(orderService).deleteOrderById(1L);
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testEditOrder_Success() throws Exception {
        Orders order = createTestOrder();
        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/employee/orders/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/editOrder"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("rows"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testChoiceAppliance_Success() throws Exception {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Test Appliance");

        when(applianceService.getAllAppliances()).thenReturn(List.of(appliance));

        mockMvc.perform(get("/employee/orders/1/choice-appliance"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/order/choiceAppliance"))
                .andExpect(model().attributeExists("appliances"))
                .andExpect(model().attributeExists("ordersId"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testApproveOrder_Success() throws Exception {
        mockMvc.perform(get("/employee/orders/1/approved"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/orders?page=0"));

        verify(orderService).setApproved(1L, true);
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testUnapproveOrder_Success() throws Exception {
        mockMvc.perform(get("/employee/orders/1/unapproved"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/orders?page=0"));

        verify(orderService).setApproved(1L, false);
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testDeleteOrderRow_Success() throws Exception {
        mockMvc.perform(get("/employee/orders/1/edit/row/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/orders/1/edit"));

        verify(orderService).deleteOrderRowById(1L);
    }
}