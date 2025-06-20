package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public Employee findById(Long id) {
        return null;
    }

    @Override
    public Employee save(Employee employee) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
