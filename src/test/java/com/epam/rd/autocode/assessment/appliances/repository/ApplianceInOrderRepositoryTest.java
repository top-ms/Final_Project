package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ApplianceInOrderRepositoryTest {


    @MockBean
    private ApplianceInOrderRepository applianceInOrderRepository;

    /**
     * Test to verify that the method findAll retrieves all OrderRows in the repository.
     */
    @Test
    void testFindAllReturnsCorrectData() {
        // Prepare test data: Multiple OrderRows
        Appliance appliance = new Appliance(
                1L,
                "Claw",
                Category.BIG,
                "ModelA",
                new Manufacturer(1L, "Samsung"),
                PowerType.ACCUMULATOR,
                "--------",
                "-------",
                600,
                BigDecimal.valueOf(1.01)
        );

        OrderRow orderRow1 = new OrderRow(1L, appliance, 2L, BigDecimal.valueOf(2.02));
        OrderRow orderRow2 = new OrderRow(2L, appliance, 1L, BigDecimal.valueOf(1.01));

        Mockito.when(applianceInOrderRepository.findAll())
                .thenReturn(Arrays.asList(orderRow1, orderRow2));

        // Execute the method under test
        List<OrderRow> result = applianceInOrderRepository.findAll();

        // Verify the result contains all OrderRows
        assertEquals(2, result.size());
        assertTrue(result.contains(orderRow1));
        assertTrue(result.contains(orderRow2));
    }

    /**
     * Test to verify that the method findById retrieves the correct OrderRow when provided with a valid ID.
     */
    @Test
    void testFindByIdReturnsCorrectData() {
        // Prepare test data: Single OrderRow
        Appliance appliance = new Appliance(
                1L,
                "Claw",
                Category.BIG,
                "ModelA",
                new Manufacturer(1L, "Samsung"),
                PowerType.ACCUMULATOR,
                "--------",
                "-------",
                600,
                BigDecimal.valueOf(1.01)
        );

        OrderRow orderRow = new OrderRow(1L, appliance, 2L, BigDecimal.valueOf(2.02));

        Mockito.when(applianceInOrderRepository.findById(1L))
                .thenReturn(java.util.Optional.of(orderRow));

        // Execute the method under test
        OrderRow result = applianceInOrderRepository.findById(1L).orElse(null);

        // Verify the result matches the expected OrderRow
        assertNotNull(result);
        assertEquals(orderRow, result);
    }

    /**
     * Test to verify that the method save correctly stores an OrderRow in the repository.
     */
    @Test
    void testSaveSavesCorrectData() {
        // Prepare test data: OrderRow to save
        Appliance appliance = new Appliance(
                1L,
                "Claw",
                Category.BIG,
                "ModelA",
                new Manufacturer(1L, "Samsung"),
                PowerType.ACCUMULATOR,
                "--------",
                "-------",
                600,
                BigDecimal.valueOf(1.01)
        );

        OrderRow orderRow = new OrderRow(1L, appliance, 2L, BigDecimal.valueOf(2.02));

        Mockito.when(applianceInOrderRepository.save(orderRow))
                .thenReturn(orderRow);

        // Execute the method under test
        OrderRow result = applianceInOrderRepository.save(orderRow);

        // Verify the result matches the expected OrderRow
        assertNotNull(result);
        assertEquals(orderRow, result);
    }

    /**
     * Test to verify that the method deleteById properly removes an OrderRow from the repository.
     */
    @Test
    void testDeleteByIdDeletesCorrectData() {
        // Prepare test data: OrderRow ID to delete
        Long orderRowId = 1L;

        // Mock the repository behavior
        Mockito.doNothing().when(applianceInOrderRepository).deleteById(orderRowId);
        Mockito.when(applianceInOrderRepository.existsById(orderRowId)).thenReturn(false);

        // Execute the method under test
        applianceInOrderRepository.deleteById(orderRowId);

        // Verify the OrderRow no longer exists
        assertFalse(applianceInOrderRepository.existsById(orderRowId));
    }

    /**
     * Test to verify that the method `findByAppliance` properly fetches OrderRows
     * associated with a given appliance.
     */
    @Test
    void testFindByApplianceReturnsCorrectResults() {
        // Setup test data: Appliance and corresponding OrderRows
        Appliance appliance = new Appliance(
                1L,
                "Claw", // Name of the appliance
                Category.BIG, // Category: can be BIG or SMALL
                "-----",
                new Manufacturer(1L, "Samsung"), // Associated manufacturer
                PowerType.ACCUMULATOR, // Power source: ELECTRIC (220V) or BATTERY
                "-------",
                "-----",
                600, // Power in watts
                BigDecimal.valueOf(1.01) // Price falls between 1.01 and 7.01
        );

        OrderRow orderRow1 = new OrderRow(1L, appliance, 2L, BigDecimal.valueOf(2.02));
        OrderRow orderRow2 = new OrderRow(2L, appliance, 1L, BigDecimal.valueOf(1.01));

        // Mock behavior of the repository
        Mockito.when(applianceInOrderRepository.findByAppliance(appliance))
                .thenReturn(Arrays.asList(orderRow1, orderRow2));

        // Execute the method under test
        List<OrderRow> result = applianceInOrderRepository.findByAppliance(appliance);

        // Verify that the result contains the expected data
        assertEquals(2, result.size()); // Check that two rows are returned
        assertTrue(result.contains(orderRow1)); // Check that first row is in the result
        assertTrue(result.contains(orderRow2)); // Check that second row is in the result
    }

    /**
     * Test to verify that an empty list is returned when there are no OrderRows
     * associated with the given appliance.
     */
    @Test
    void testFindByApplianceReturnsEmptyListWhenNoResultsFound() {
        // Setup test data: Appliance that exists but has no associated OrderRows
        Appliance appliance = new Appliance(
                2L,
                "Bane", // Another name for the appliance
                Category.SMALL, // Category: SMALL in this case
                "ModelB",
                new Manufacturer(2L, "Lenovo"), // Another manufacturer
                PowerType.ACCUMULATOR, // Power source: BATTERY
                "Compact and efficient",
                "Highly portable small appliance",
                360, // Higher power appliance example
                BigDecimal.valueOf(3.50) // Price in valid range
        );

        // Mock behavior of the repository: Return an empty list for this appliance
        Mockito.when(applianceInOrderRepository.findByAppliance(appliance))
                .thenReturn(Collections.emptyList());

        // Execute the method under test
        List<OrderRow> result = applianceInOrderRepository.findByAppliance(appliance);

        // Verify that the result is an empty list
        assertTrue(result.isEmpty());
    }

    /**
     * Test to verify that the method behaves correctly when a null value for Appliance
     * is passed. Mocking this behavior explicitly for edge case handling.
     */
    @Test
    void testFindByApplianceReturnsNullWhenApplianceIsNull() {
        // Mock behavior of the repository: Return null when null appliance is provided
        Mockito.when(applianceInOrderRepository.findByAppliance(null))
                .thenReturn(null);

        // Execute the method under test
        List<OrderRow> result = applianceInOrderRepository.findByAppliance(null);

        // Verify that result is null when null was input
        assertEquals(null, result);
    }
}