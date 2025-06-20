package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> findAll();
    Orders findById(Long id);
    Orders save(Orders order);
    void deleteById(Long id);
}