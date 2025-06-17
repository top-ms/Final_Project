package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
public class OrdersRepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * Test for the findByClient method in OrdersRepository.
     * This method retrieves all orders associated with a specific client.
     */

    @Test
    void testFindByClientWithExistingClient() {
        // Arrange
        Client client = new Client(1L, "John Doe", "john.doe@example.com", "password123", "CARD123");
        Employee employee = new Employee(2L, "Jane Smith", "jane.smith@example.com", "password456", "DEPARTMENT_A");
        Orders order = new Orders(1L, client, employee, Collections.emptySet(), true);
        Orders order2 = new Orders(2L, client, employee, Collections.emptySet(), false);

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByClient(client)).thenReturn(Arrays.asList(order, order2));

        // Act
        List<Orders> result = mockRepository.findByClient(client);

        // Assert
        assertEquals(2, result.size());
        assertEquals(order, result.get(0));
        assertEquals(order2, result.get(1));
    }

    @Test
    void testFindByClientWithNoOrdersForClient() {
        // Arrange
        Client client = new Client(1L, "John Doe", "john.doe@example.com", "password123", "CARD123");

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByClient(client)).thenReturn(Collections.emptyList());

        // Act
        List<Orders> result = mockRepository.findByClient(client);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testFindByClientWithNullClient() {
        // Arrange
        Client client = null;

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByClient(client)).thenReturn(Collections.emptyList());

        // Act
        List<Orders> result = mockRepository.findByClient(client);

        // Assert
        assertEquals(0, result.size());
    }


    @Test
    void testFindByEmployeeWithExistingEmployee() {
        // Arrange
        Client client = new Client(1L, "John Doe", "john.doe@example.com", "password123", "CARD123");
        Employee employee = new Employee(2L, "Jane Smith", "jane.smith@example.com", "password456", "DEPARTMENT_A");
        Orders order = new Orders(1L, client, employee, Collections.emptySet(), true);
        Orders order2 = new Orders(2L, client, employee, Collections.emptySet(), false);

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByEmployee(employee)).thenReturn(Arrays.asList(order, order2));

        // Act
        List<Orders> result = mockRepository.findByEmployee(employee);

        // Assert
        assertEquals(2, result.size());
        assertEquals(order, result.get(0));
        assertEquals(order2, result.get(1));
    }

    @Test
    void testFindByEmployeeWithNoOrders() {
        // Arrange
        Employee employee = new Employee(2L, "Jane Smith", "jane.smith@example.com", "password456", "DEPARTMENT_A");

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByEmployee(employee)).thenReturn(Collections.emptyList());

        // Act
        List<Orders> result = mockRepository.findByEmployee(employee);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testFindByEmployeeWithNullEmployee() {
        // Arrange
        Employee employee = null;

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByEmployee(employee)).thenReturn(Collections.emptyList());

        // Act
        List<Orders> result = mockRepository.findByEmployee(employee);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void testFindByApprovedWithTrue() {
        // Arrange
        Client client = new Client(1L, "John Doe", "john.doe@example.com", "password123", "CARD123");
        Employee employee = new Employee(2L, "Jane Smith", "jane.smith@example.com", "password456", "DEPARTMENT_A");
        Orders order = new Orders(1L, client, employee, Collections.emptySet(), true);

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByApproved(true)).thenReturn(Collections.singletonList(order));

        // Act
        List<Orders> result = mockRepository.findByApproved(true);

        // Assert
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    void testFindByApprovedWithFalse() {
        // Arrange
        Client client = new Client(1L, "John Doe", "john.doe@example.com", "password123", "CARD123");
        Employee employee = new Employee(2L, "Jane Smith", "jane.smith@example.com", "password456", "DEPARTMENT_A");
        Orders order = new Orders(1L, client, employee, Collections.emptySet(), false);

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByApproved(false)).thenReturn(Collections.singletonList(order));

        // Act
        List<Orders> result = mockRepository.findByApproved(false);

        // Assert
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    void testFindByApprovedWithNullApproved() {
        // Arrange
        Boolean approved = null;

        OrdersRepository mockRepository = Mockito.mock(OrdersRepository.class);
        when(mockRepository.findByApproved(approved)).thenReturn(Collections.emptyList());

        // Act
        List<Orders> result = mockRepository.findByApproved(approved);

        // Assert
        assertEquals(0, result.size());
    }
}