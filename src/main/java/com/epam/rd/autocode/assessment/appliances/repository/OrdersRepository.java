package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByEmployeeId(Long id);

    List<Orders> findAllByClientId(Long id);





    // Пошук за ID замовлення
    Optional<Orders> findById(Long id);

    // Фільтрація за працівником
    Page<Orders> findByEmployeeId(Long employeeId, Pageable pageable);

    // Фільтрація за статусом approved
    Page<Orders> findByApproved(Boolean approved, Pageable pageable);

    // Комбінована фільтрація за працівником та статусом
    Page<Orders> findByEmployeeIdAndApproved(Long employeeId, Boolean approved, Pageable pageable);

    // Отримання всіх замовлень з пагінацією
    Page<Orders> findAll(Pageable pageable);




    /// //
}
