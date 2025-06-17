package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {

    // Пошук за назвою
    List<Appliance> findByName(String name);

    // Пошук за категорією (BIG, SMALL)
    List<Appliance> findByCategory(Category category);

    // Пошук за моделлю
    List<Appliance> findByModel(String model);

    // Пошук за типом живлення (AC110, ACCUMULATOR, AC220)
    List<Appliance> findByPowerType(PowerType powerType);

    // Пошук за потужністю
    List<Appliance> findByPower(Integer power);

    // Пошук за ціною
    List<Appliance> findByPrice(BigDecimal price);
}
