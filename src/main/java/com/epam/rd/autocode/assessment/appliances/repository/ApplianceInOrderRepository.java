package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplianceInOrderRepository extends JpaRepository<OrderRow, Long> {
        List<OrderRow> findByAppliance(Appliance appliance);
}
