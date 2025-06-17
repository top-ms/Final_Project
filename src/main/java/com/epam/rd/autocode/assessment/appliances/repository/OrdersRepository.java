package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // Пошук усіх замовлень певного клієнта
    List<Orders> findByClient(Client client);

    // Пошук усіх замовлень оброблених певним працівником
    List<Orders> findByEmployee(Employee employee);

    // Пошук за статусом підтвердження
    List<Orders> findByApproved(Boolean approved);
}
