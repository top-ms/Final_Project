package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Знайти всі замовлення конкретного клієнта
     */
    Page<Orders> findByClientId(Long clientId, Pageable pageable);

    /**
     * Знайти замовлення клієнта за статусом затвердження
     */
    Page<Orders> findByClientIdAndApproved(Long clientId, Boolean approved, Pageable pageable);

    /**
     * Знайти замовлення клієнта за працівником
     */
    Page<Orders> findByClientIdAndEmployeeId(Long clientId, Long employeeId, Pageable pageable);

    /**
     * Знайти замовлення клієнта за працівником і статусом затвердження
     */
    Page<Orders> findByClientIdAndEmployeeIdAndApproved(Long clientId, Long employeeId, Boolean approved, Pageable pageable);

    /**
     * Альтернативний варіант з використанням @Query (якщо потрібно)
     */
    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId")
    Page<Orders> findOrdersByClientId(@Param("clientId") Long clientId, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId AND o.approved = :approved")
    Page<Orders> findOrdersByClientIdAndApproved(@Param("clientId") Long clientId,
                                                 @Param("approved") Boolean approved,
                                                 Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId AND o.employee.id = :employeeId")
    Page<Orders> findOrdersByClientIdAndEmployeeId(@Param("clientId") Long clientId,
                                                   @Param("employeeId") Long employeeId,
                                                   Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.client.id = :clientId AND o.employee.id = :employeeId AND o.approved = :approved")
    Page<Orders> findOrdersByClientIdAndEmployeeIdAndApproved(@Param("clientId") Long clientId,
                                                              @Param("employeeId") Long employeeId,
                                                              @Param("approved") Boolean approved,
                                                              Pageable pageable);
}
