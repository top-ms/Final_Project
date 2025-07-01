package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final ApplianceRepository applianceRepository;
    private final OrderRowRepository orderRowRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository, ApplianceRepository applianceRepository, OrderRowRepository orderRowRepository) {
        this.ordersRepository = ordersRepository;
        this.applianceRepository = applianceRepository;
        this.orderRowRepository = orderRowRepository;
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public void saveNewOrder(Orders order) {
        ordersRepository.save(order);
    }

    public void deleteOrderById(Long id) {  ordersRepository.deleteById(id);}

    @Override
    public void saveNewOrderRowById(Long orderId, Long applianceId, Long number) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not find"));

        Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new IllegalArgumentException("Appliance not find"));

        OrderRow orderRow = new OrderRow();
        orderRow.setOrder(orders);
        orderRow.setAppliance(appliance);
        orderRow.setNumber(number);
        orderRow.setAmount(appliance.getPrice().multiply(BigDecimal.valueOf(number)));


        orders.getOrderRowSet().add(orderRow);
        ordersRepository.save(orders);
    }

    @Override
    public Orders findById(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id = " + id));
    }

    @Override
    public void setApproved(Long id, boolean status) {
        Orders order = ordersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setApproved(status);
        ordersRepository.save(order);
    }

    public void deleteOrderRowById(Long rowId) {
        orderRowRepository.deleteById(rowId);
    }
}
