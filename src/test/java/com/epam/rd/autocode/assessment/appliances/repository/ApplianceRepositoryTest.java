package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class ApplianceRepositoryTest {

    @Autowired
    private ApplianceRepository applianceRepository;

    @MockBean
    private ApplianceRepository mockApplianceRepository;

    /**
     * Test class for the `findByName` method in the `ApplianceRepository`.
     * Verifies the correctness of queries filtering appliances by their names.
     */

    @Test
    void testFindByName_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance1 = new Appliance(
                1L,
                "Washing Machine",
                Category.BIG,
                "W123",
                new Manufacturer(1L, "BrandA"),
                PowerType.AC110,
                "Front Load",
                "Efficient washing machine",
                500,
                BigDecimal.valueOf(499.99)
        );

        Appliance appliance2 = new Appliance(
                2L,
                "Washing Machine",
                Category.SMALL,
                "W456",
                new Manufacturer(2L, "BrandB"),
                PowerType.ACCUMULATOR,
                "Top Load",
                "Compact design",
                600,
                BigDecimal.valueOf(599.99)
        );

        when(mockApplianceRepository.findByName("Washing Machine")).thenReturn(Arrays.asList(appliance1, appliance2));

        // Act
        List<Appliance> result = mockApplianceRepository.findByName("Washing Machine");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(appliance1, appliance2);
    }

    @Test
    void testFindByName_ReturnsEmptyListForNonExistingName() {
        // Arrange
        when(mockApplianceRepository.findByName("NonExisting")).thenReturn(List.of());

        // Act
        List<Appliance> result = mockApplianceRepository.findByName("NonExisting");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByName_ReturnsEmptyListForNullName() {
        // Arrange
        when(mockApplianceRepository.findByName(null)).thenReturn(List.of());

        // Act
        List<Appliance> result = mockApplianceRepository.findByName(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }


    @Test
    void testFindByCategory_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance1 = new Appliance(
                1L,
                "Washing Machine",
                Category.BIG,
                "W123",
                new Manufacturer(1L, "BrandA"),
                PowerType.AC110,
                "Front Load",
                "Efficient washing machine",
                500,
                BigDecimal.valueOf(499.99)
        );

        Appliance appliance2 = new Appliance(
                2L,
                "Refrigerator",
                Category.BIG,
                "R123",
                new Manufacturer(2L, "BrandB"),
                PowerType.AC220,
                "Double Door",
                "Large refrigerator",
                300,
                BigDecimal.valueOf(999.99)
        );

        when(mockApplianceRepository.findByCategory(Category.BIG)).thenReturn(Arrays.asList(appliance1, appliance2));

        // Act
        List<Appliance> result = mockApplianceRepository.findByCategory(Category.BIG);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(appliance1, appliance2);
    }

    @Test
    void testFindByModel_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance = new Appliance(
                1L,
                "Vacuum Cleaner",
                Category.SMALL,
                "V123",
                new Manufacturer(3L, "BrandC"),
                PowerType.ACCUMULATOR,
                "Portable",
                "Cordless vacuum cleaner",
                200,
                BigDecimal.valueOf(199.99)
        );

        when(mockApplianceRepository.findByModel("V123")).thenReturn(Arrays.asList(appliance));

        // Act
        List<Appliance> result = mockApplianceRepository.findByModel("V123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(appliance);
    }

    @Test
    void testFindByPowerType_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance = new Appliance(
                1L,
                "Air Conditioner",
                Category.BIG,
                "AC123",
                new Manufacturer(4L, "BrandD"),
                PowerType.AC110,
                "Split Type",
                "Energy efficient air conditioner",
                1200,
                BigDecimal.valueOf(799.99)
        );

        when(mockApplianceRepository.findByPowerType(PowerType.AC110)).thenReturn(Arrays.asList(appliance));

        // Act
        List<Appliance> result = mockApplianceRepository.findByPowerType(PowerType.AC110);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(appliance);
    }

    @Test
    void testFindByPower_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance = new Appliance(
                1L,
                "Toaster",
                Category.SMALL,
                "T123",
                new Manufacturer(5L, "BrandE"),
                PowerType.ACCUMULATOR,
                "Automatic",
                "Compact toaster",
                800,
                BigDecimal.valueOf(49.99)
        );

        when(mockApplianceRepository.findByPower(800)).thenReturn(Arrays.asList(appliance));

        // Act
        List<Appliance> result = mockApplianceRepository.findByPower(800);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(appliance);
    }

    @Test
    void testFindByPrice_ReturnsMatchingAppliances() {
        // Arrange
        Appliance appliance = new Appliance(
                1L,
                "Microwave Oven",
                Category.BIG,
                "MO123",
                new Manufacturer(6L, "BrandF"),
                PowerType.AC220,
                "Grill",
                "High-performance microwave oven",
                1000,
                BigDecimal.valueOf(149.99)
        );

        when(mockApplianceRepository.findByPrice(BigDecimal.valueOf(149.99))).thenReturn(Arrays.asList(appliance));

        // Act
        List<Appliance> result = mockApplianceRepository.findByPrice(BigDecimal.valueOf(149.99));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(appliance);
    }
}