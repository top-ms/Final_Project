package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> getAllOrders();

    void saveNewOrder(Orders order);

    void deleteOrderById(Long id);

    void deleteOrderRowById(Long id);

    void saveNewOrderRowById(Long orderId, Long applianceId, Long number);

    Orders findById(Long id);

    void setApproved(Long id, boolean status);
}