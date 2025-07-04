package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    List<Appliance> findAllByManufacturerId(Long manufacturerId);
    Optional<Appliance> findByName(String name);
}
