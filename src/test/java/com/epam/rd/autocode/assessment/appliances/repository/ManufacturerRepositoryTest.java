package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    /**
     * Тест перевіряє, що виробник з ім’ям "Samsung" існує у базі даних,
     * і метод findByName(name) повертає Optional з правильним об'єктом.
     */
    @Test
    void shouldReturnManufacturerWhenNameExists() {
        // Act

        Optional<Manufacturer> result = manufacturerRepository.findByName("Samsung");

        // Assert
        assertThat(result).isPresent(); // Перевіряємо, що значення є
        assertThat(result.get().getName()).isEqualTo("Samsung"); // Перевіряємо правильність імені
    }

    /**
     * Тест перевіряє ситуацію, коли виробника з таким іменем не існує.
     * Метод повинен повернути порожній Optional.
     */
    @Test
    void shouldReturnEmptyWhenNameDoesNotExist() {
        // Act
        Optional<Manufacturer> result = manufacturerRepository.findByName("NonExistingName");

        // Assert
        assertThat(result).isNotPresent(); // Очікуємо відсутність значення
    }

    /**
     * Тест перевіряє, що запит з null-значенням не кидає виключення,
     * і метод повертає порожній результат.
     */
    @Test
    void shouldReturnEmptyWhenNameIsNull() {
        // Act
        Optional<Manufacturer> result = manufacturerRepository.findByName(null);

        // Assert
        assertThat(result).isNotPresent();
    }


    /**
     * Тест перевіряє, що метод findAll() повертає всі об'єкти виробників з бази даних.
     */
    @Test
    void shouldReturnAllManufacturersWhenFindAllIsCalled() {
        // Act
        var manufacturers = manufacturerRepository.findAll();

        // Assert
        assertThat(manufacturers).isNotEmpty(); // Перевіряємо, що список не порожній
        assertThat(manufacturers.size()).isEqualTo(7); // Перевіряємо, що список містить очікувану кількість елементів
    }

    /**
     * Тест перевіряє, що метод findById() повертає правильний об'єкт виробника за заданим ID.
     */
    @Test
    void shouldReturnManufacturerWhenIdExists() {
        // Act
        Optional<Manufacturer> result = manufacturerRepository.findById(1L);

        // Assert
        assertThat(result).isPresent(); // Перевіряємо, що значення є
        assertThat(result.get().getName()).isEqualTo("Samsung"); // Перевіряємо правильність імені
    }

    /**
     * Тест перевіряє, що метод save() зберігає новий об'єкт виробника в базу даних.
     */
    @Test
    void shouldSaveManufacturerWhenSaveIsCalled() {
        // Arrange
        Manufacturer newManufacturer = new Manufacturer();
        newManufacturer.setName("TestManufacturer");

        // Act
        Manufacturer savedManufacturer = manufacturerRepository.save(newManufacturer);

        // Assert
        assertThat(manufacturerRepository.findById(savedManufacturer.getId())).isPresent(); // Перевіряємо, що новий виробник є в базі
        assertThat(savedManufacturer.getName()).isEqualTo("TestManufacturer"); // Перевіряємо, що ім'я збережено правильно
    }

    /**
     * Тест перевіряє, що метод deleteById() видаляє виробника за заданим ID.
     */
    @Test
    void shouldDeleteManufacturerWhenDeleteByIdIsCalled() {
        // Arrange
        Long manufacturerId = 1L;

        // Act
        manufacturerRepository.deleteById(manufacturerId);

        // Assert
        assertThat(manufacturerRepository.findById(manufacturerId)).isNotPresent(); // Перевіряємо, що виробника немає
    }
}
