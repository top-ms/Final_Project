package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getAllOrders();

    void saveNewOrder(Orders order);

    void deleteOrderById(Long id);

    void deleteOrderRowById(Long id);

    void saveNewOrderRowById(Long orderId, Long applianceId, Long number);

    Orders findById(Long id);

    void setApproved(Long id, boolean status);





    // Нові методи для пагінації та фільтрації
    Page<ViewOrdersDTO> getAllOrdersAsDto(Pageable pageable);

    Page<ViewOrdersDTO> getOrdersByEmployeeId(Long employeeId, Pageable pageable);

    Page<ViewOrdersDTO> getOrdersByApproved(Boolean approved, Pageable pageable);

    Page<ViewOrdersDTO> getOrdersByEmployeeIdAndApproved(Long employeeId, Boolean approved, Pageable pageable);

    Optional<ViewOrdersDTO> findOrderDtoById(Long id);


    Page<ViewOrdersDTO> getOrdersByClientId(Long clientId, Pageable pageable);

    /**
     * Отримати замовлення клієнта за статусом затвердження
     */
    Page<ViewOrdersDTO> getOrdersByClientIdAndApproved(Long clientId, Boolean approved, Pageable pageable);

    /**
     * Отримати замовлення клієнта за працівником
     */
    Page<ViewOrdersDTO> getOrdersByClientIdAndEmployeeId(Long clientId, Long employeeId, Pageable pageable);

    /**
     * Отримати замовлення клієнта за працівником і статусом затвердження
     */
    Page<ViewOrdersDTO> getOrdersByClientIdAndEmployeeIdAndApproved(Long clientId, Long employeeId, Boolean approved, Pageable pageable);
}