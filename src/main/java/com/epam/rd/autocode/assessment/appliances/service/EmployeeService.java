package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService{


    List<Employee> getAllEmployee();

    void deleteEmployeeById(Long id);
    void deleteAllOrdersOfEmployeeById(Long id);
    void register(Employee employee);
}