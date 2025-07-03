package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.EmployeeRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, OrdersRepository ordersRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.ordersRepository = ordersRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }





    @Override
    public Optional<ViewEmployeesDTO> findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(employee -> modelMapper.map(employee, ViewEmployeesDTO.class));
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }

    @Override
    public Page<ViewEmployeesDTO> getAllEmployeesAsDto(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employee -> modelMapper.map(employee, ViewEmployeesDTO.class));
    }


















    @Override
    public List<Employee> getAllEmployees() {
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

    @Override
    public void register(EmployeeRegisterDTO dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setDepartment("sales");
        employeeRepository.save(employee);
        System.out.println("Registered client: " + employee.getEmail() + " with password: " + employee.getPassword() + "");
    }
}
