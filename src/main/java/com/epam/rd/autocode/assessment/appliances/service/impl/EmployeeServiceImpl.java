package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrdersRepository ordersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, OrdersRepository ordersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.ordersRepository = ordersRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
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


    // ✅ Метод для реєстрації користувача з хешуванням пароля і генерацією карти
    @Override
    public void register(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        System.out.println("Registered client: " + employee.getEmail() + " with password: " + employee.getPassword() + "");
    }
}
