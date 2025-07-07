package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.EmployeeRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private ViewEmployeesDTO createTestEmployeeDTO() {
        ViewEmployeesDTO dto = new ViewEmployeesDTO();
        dto.setId(1L);
        dto.setName("Test Employee");
        dto.setEmail("employee@test.com");
        return dto;
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test Employee");
        employee.setEmail("employee@test.com");
        return employee;
    }

    private EmployeeRegisterDTO createTestEmployeeRegisterDTO() {
        EmployeeRegisterDTO dto = new EmployeeRegisterDTO();
        dto.setName("New Employee");
        dto.setEmail("newemployee@test.com");
        dto.setPassword("password123");
        return dto;
    }

    private UserEditDTO createTestUserEditDTO() {
        UserEditDTO dto = new UserEditDTO();
        dto.setId(1L);
        dto.setName("Updated Employee");
        dto.setEmail("updated@test.com");
        return dto;
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfEmployees_Success() throws Exception {
        ViewEmployeesDTO employeeDTO = createTestEmployeeDTO();
        Page<ViewEmployeesDTO> employeesPage = new PageImpl<>(List.of(employeeDTO));

        when(employeeService.getAllEmployeesAsDto(any(Pageable.class))).thenReturn(employeesPage);

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/employees"))
                .andExpect(model().attributeExists("employeesPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfEmployees_WithPagination() throws Exception {
        ViewEmployeesDTO employeeDTO = createTestEmployeeDTO();
        Page<ViewEmployeesDTO> employeesPage = new PageImpl<>(List.of(employeeDTO));

        when(employeeService.getAllEmployeesAsDto(any(Pageable.class))).thenReturn(employeesPage);

        mockMvc.perform(get("/admin/employees")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/employees"))
                .andExpect(model().attributeExists("employeesPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchEmployees_Success() throws Exception {
        ViewEmployeesDTO employeeDTO = createTestEmployeeDTO();
        when(employeeService.findByEmail("employee@test.com")).thenReturn(Optional.of(employeeDTO));

        mockMvc.perform(get("/admin/employees/search")
                        .param("email", "employee@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/employees"))
                .andExpect(model().attributeExists("employeesPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchEmployees_NotFound() throws Exception {
        when(employeeService.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/employees/search")
                        .param("email", "notfound@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/employees"))
                .andExpect(model().attributeExists("notFound"))
                .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchEmployees_EmptyEmail() throws Exception {
        mockMvc.perform(get("/admin/employees/search")
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowFormForAddingEmployee() throws Exception {
        mockMvc.perform(get("/admin/employees/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/newEmployee"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewEmployee_Success() throws Exception {
        when(employeeService.existsByEmail("newemployee@test.com")).thenReturn(false);

        mockMvc.perform(post("/admin/employees/add-employee")
                        .with(csrf())
                        .param("name", "New Employee")
                        .param("email", "newemployee@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).register(any(EmployeeRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewEmployee_EmailExists() throws Exception {
        when(employeeService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/employees/add-employee")
                        .with(csrf())
                        .param("name", "New Employee")
                        .param("email", "existing@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/newEmployee"));

        verify(employeeService, never()).register(any(EmployeeRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteEmployee_Success() throws Exception {
        mockMvc.perform(get("/admin/employees/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees?page=0"));

        verify(employeeService).deleteEmployeeById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteEmployee_WithPage() throws Exception {
        mockMvc.perform(get("/admin/employees/1/delete")
                        .param("page", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees?page=2"));

        verify(employeeService).deleteEmployeeById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditEmployeeForm_Success() throws Exception {
        UserEditDTO userEditDTO = createTestUserEditDTO();
        when(employeeService.findByIdForEdit(1L)).thenReturn(Optional.of(userEditDTO));

        mockMvc.perform(get("/admin/employees/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/editEmployee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditEmployeeForm_EmployeeNotFound() throws Exception {
        when(employeeService.findByIdForEdit(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/employees/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateEmployee_EmployeeNotFound() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/employees/1/update")
                        .with(csrf())
                        .param("name", "Updated Employee")
                        .param("email", "updated@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService, never()).updateEmployee(any(UserEditDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateEmployee_EmailExists() throws Exception {
        Employee existingEmployee = createTestEmployee();
        existingEmployee.setEmail("old@test.com");
        when(employeeService.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/employees/1/update")
                        .with(csrf())
                        .param("name", "Updated Employee")
                        .param("email", "existing@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employee/editEmployee"))
                .andExpect(model().attribute("isEdit", true));

        verify(employeeService, never()).updateEmployee(any(UserEditDTO.class));
    }
}
