package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeRepositoryTest {

    @Test
    void testFindByNameReturnsSpecificEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        String nameToSearch = "Phobos";

        Employee expectedEmployee = new Employee(null, "Phobos", "phobos@gmail.com", "111", "salle");
        List<Employee> expectedEmployees = Collections.singletonList(expectedEmployee);
        when(employeeRepository.findByName(nameToSearch)).thenReturn(expectedEmployees);

        // Act
        List<Employee> result = employeeRepository.findByName(nameToSearch);

        // Assert
        assertEquals(expectedEmployees, result, "Expected one employee with the name 'Phobos' to be returned.");
        verify(employeeRepository, times(1)).findByName(nameToSearch);
    }

    @Test
    void testFindByEmailReturnsSpecificEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        String emailToSearch = "phobos@gmail.com";

        Employee expectedEmployee = new Employee(null, "Phobos", "phobos@gmail.com", "111", "salle");
        List<Employee> expectedEmployees = Collections.singletonList(expectedEmployee);
        when(employeeRepository.findByEmail(emailToSearch)).thenReturn(expectedEmployees);

        // Act
        List<Employee> result = employeeRepository.findByEmail(emailToSearch);

        // Assert
        assertEquals(expectedEmployees, result, "Expected one employee with the email 'phobos@gmail.com' to be returned.");
        verify(employeeRepository, times(1)).findByEmail(emailToSearch);
    }

    @Test
    void testFindByDepartmentReturnsSpecificEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        String departmentToSearch = "salle";

        Employee expectedEmployee = new Employee(null, "Phobos", "phobos@gmail.com", "111", "salle");
        List<Employee> expectedEmployees = Collections.singletonList(expectedEmployee);
        when(employeeRepository.findByDepartment(departmentToSearch)).thenReturn(expectedEmployees);

        // Act
        List<Employee> result = employeeRepository.findByDepartment(departmentToSearch);

        // Assert
        assertEquals(expectedEmployees, result, "Expected one employee in department 'salle' to be returned.");
        verify(employeeRepository, times(1)).findByDepartment(departmentToSearch);
    }

    @Test
    void testFindByNameReturnsEmptyListWhenNotFound() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.findByName("Unknown")).thenReturn(Collections.emptyList());

        // Act
        List<Employee> result = employeeRepository.findByName("Unknown");

        // Assert
        assertEquals(Collections.emptyList(), result, "Expected no employees to be returned.");
        verify(employeeRepository, times(1)).findByName("Unknown");
    }

    @Test
    void testFindAllReturnsAllEmployees() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);

        Employee employee1 = new Employee(1L, "Phobos", "phobos@gmail.com", "111", "salle");
        Employee employee2 = new Employee(2L, "Deimos", "deimos@gmail.com", "222", "security");
        List<Employee> expectedEmployees = List.of(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // Act
        List<Employee> result = employeeRepository.findAll();

        // Assert
        assertEquals(expectedEmployees, result, "Expected all employees to be returned.");
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdReturnsSpecificEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        Long idToSearch = 1L;

        Employee expectedEmployee = new Employee(1L, "Phobos", "phobos@gmail.com", "111", "salle");
        when(employeeRepository.findById(idToSearch)).thenReturn(java.util.Optional.of(expectedEmployee));

        // Act
        Employee result = employeeRepository.findById(idToSearch).orElse(null);

        // Assert
        assertEquals(expectedEmployee, result, "Expected the employee with ID 1 to be returned.");
        verify(employeeRepository, times(1)).findById(idToSearch);
    }

    @Test
    void testSaveStoresEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        Employee employeeToSave = new Employee(null, "Europa", "europa@gmail.com", "333", "finance");

        Employee savedEmployee = new Employee(1L, "Europa", "europa@gmail.com", "333", "finance");
        when(employeeRepository.save(employeeToSave)).thenReturn(savedEmployee);

        // Act
        Employee result = employeeRepository.save(employeeToSave);

        // Assert
        assertEquals(savedEmployee, result, "Expected the employee to be saved and returned with an ID.");
        verify(employeeRepository, times(1)).save(employeeToSave);
    }

    @Test
    void testDeleteByIdRemovesEmployee() {
        // Arrange
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        Long idToDelete = 1L;

        // Act
        employeeRepository.deleteById(idToDelete);

        // Assert
        verify(employeeRepository, times(1)).deleteById(idToDelete);
    }

    //---------------------------------
}
