package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.EmployeeRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;
    private OrdersRepository ordersRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        ordersRepository = mock(OrdersRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        modelMapper = new ModelMapper();
        employeeService = new EmployeeServiceImpl(employeeRepository, ordersRepository, passwordEncoder, modelMapper);
    }

    @Test
    void testRegister() {
        EmployeeRegisterDTO dto = new EmployeeRegisterDTO("John", "john@example.com", "pass123", null);
        String encodedPassword = "encodedPass";
        when(passwordEncoder.encode("pass123")).thenReturn(encodedPassword);

        employeeService.register(dto);

        verify(employeeRepository).save(argThat(emp ->
                emp.getName().equals("John") &&
                        emp.getEmail().equals("john@example.com") &&
                        emp.getPassword().equals(encodedPassword) &&
                        emp.getDepartment().equals("sales")
        ));
    }

    @Test
    void testFindByEmail() {
        Employee employee = new Employee();
        employee.setEmail("admin@example.com");
        when(employeeRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(employee));

        Optional<ViewEmployeesDTO> result = employeeService.findByEmail("admin@example.com");

        assertTrue(result.isPresent());
        assertEquals("admin@example.com", result.get().getEmail());
    }

    @Test
    void testExistsByEmail() {
        when(employeeRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(new Employee()));

        boolean exists = employeeService.existsByEmail("admin@example.com");

        assertTrue(exists);
    }

    @Test
    void testDeleteEmployeeById() {
        Long id = 1L;
        when(ordersRepository.findAllByEmployeeId(id)).thenReturn(List.of(new Orders()));

        employeeService.deleteEmployeeById(id);

        verify(ordersRepository).deleteAll(any());
        verify(employeeRepository).deleteById(id);
    }

    @Test
    void testUpdateEmployee() {
        Long id = 1L;
        UserEditDTO dto = new UserEditDTO(id, "Updated", "new@example.com", "newpass");

        Employee employee = new Employee();
        employee.setId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");

        employeeService.updateEmployee(dto);

        verify(employeeRepository).save(argThat(e ->
                e.getName().equals("Updated") &&
                        e.getEmail().equals("new@example.com") &&
                        e.getPassword().equals("encodedPass")
        ));
    }

    @Test
    void testFindByIdForEdit() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("Alice");
        employee.setEmail("alice@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        Optional<UserEditDTO> result = employeeService.findByIdForEdit(id);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testGetAllEmployeesAsDto() {
        Employee employee = new Employee();
        employee.setName("Alex");
        employee.setEmail("alex@example.com");

        Page<Employee> page = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ViewEmployeesDTO> result = employeeService.getAllEmployeesAsDto(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Alex", result.getContent().get(0).getName());
    }

}