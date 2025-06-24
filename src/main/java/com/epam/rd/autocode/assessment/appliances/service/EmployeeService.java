package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Employee;

import java.util.List;

public interface EmployeeService{

    List<Employee> getAllEmployee();
    void addNewEmployee(Employee employee);
    void deleteEmployeeById(Long id);
    void deleteAllOrdersOfEmployeeById(Long id);
}