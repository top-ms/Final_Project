package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrdersRepository ordersRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, OrdersRepository ordersRepository) {
        this.employeeRepository = employeeRepository;
        this.ordersRepository = ordersRepository;
    }


    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public void addNewEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        deleteAllOrdersOfEmployeeById(id);
        employeeRepository.deleteById(id);
    }

    @Override
    public void deleteAllOrdersOfEmployeeById(Long id) {
        List<Orders> orders = ordersRepository.findAllByEmployeeId(id);
        ordersRepository.deleteAll(orders);
    }
}
