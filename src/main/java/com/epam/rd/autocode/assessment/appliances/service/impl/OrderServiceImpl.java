package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Orders> findAll() {
        return List.of();
    }

    @Override
    public Orders findById(Long id) {
        return null;
    }

    @Override
    public Orders save(Orders order) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
