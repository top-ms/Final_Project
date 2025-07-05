package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderRowRepository orderRowRepository;
    private final ApplianceRepository applianceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository,
                            OrderRowRepository orderRowRepository,
                            ApplianceRepository applianceRepository,
                            ModelMapper modelMapper) {
        this.ordersRepository = ordersRepository;
        this.orderRowRepository = orderRowRepository;
        this.applianceRepository = applianceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public void saveNewOrder(Orders order) {
        ordersRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public void deleteOrderRowById(Long id) {
        orderRowRepository.deleteById(id);
    }

    @Override
    public void saveNewOrderRowById(Long orderId, Long applianceId, Long number) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Appliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new RuntimeException("Appliance not found"));

        OrderRow orderRow = new OrderRow();
        orderRow.setOrder(order);
        orderRow.setAppliance(appliance);
        orderRow.setNumber(number);

        orderRowRepository.save(orderRow);
    }

    @Override
    public Orders findById(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void setApproved(Long id, boolean status) {
        Orders order = findById(id);
        order.setApproved(status);
        ordersRepository.save(order);
    }

    @Override
    public Page<ViewOrdersDTO> getAllOrdersAsDto(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(this::convertToDto);
    }

    @Override
    public Page<ViewOrdersDTO> getOrdersByEmployeeId(Long employeeId, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findByEmployeeId(employeeId, pageable);
        return ordersPage.map(this::convertToDto);
    }

    @Override
    public Page<ViewOrdersDTO> getOrdersByApproved(Boolean approved, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findByApproved(approved, pageable);
        return ordersPage.map(this::convertToDto);
    }

    @Override
    public Page<ViewOrdersDTO> getOrdersByEmployeeIdAndApproved(Long employeeId, Boolean approved, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findByEmployeeIdAndApproved(employeeId, approved, pageable);
        return ordersPage.map(this::convertToDto);
    }

    @Override
    public Optional<ViewOrdersDTO> findOrderDtoById(Long id) {
        return ordersRepository.findById(id).map(this::convertToDto);
    }

    private ViewOrdersDTO convertToDto(Orders order) {
        ViewOrdersDTO dto = modelMapper.map(order, ViewOrdersDTO.class);

        // Встановлюємо імена клієнта та працівника
        if (order.getClient() != null) {
            dto.setClientName(order.getClient().getName());
            dto.setClientEmail(order.getClient().getEmail());
        }

        if (order.getEmployee() != null) {
            dto.setEmployeeName(order.getEmployee().getName());
        }

        // Обчислюємо загальну ціну замовлення
        BigDecimal totalPrice = calculateTotalPrice(order);
        dto.setPrice(totalPrice);

        return dto;
    }

    private BigDecimal calculateTotalPrice(Orders order) {
        BigDecimal total = BigDecimal.ZERO;
        if (order.getOrderRowSet() != null) {
            for (OrderRow row : order.getOrderRowSet()) {
                if (row.getAppliance() != null && row.getAppliance().getPrice() != null) {
                    BigDecimal rowTotal = row.getAppliance().getPrice()
                            .multiply(BigDecimal.valueOf(row.getNumber()));
                    total = total.add(rowTotal);
                }
            }
        }
        return total;
    }
}