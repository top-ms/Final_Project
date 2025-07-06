package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.EmployeeRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService{

    Optional<ViewEmployeesDTO> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<ViewEmployeesDTO> getAllEmployeesAsDto(Pageable pageable);

    List<Employee> getAllEmployees();

    void deleteEmployeeById(Long id);

    void deleteAllOrdersOfEmployeeById(Long id);

    void register(EmployeeRegisterDTO employee);

    // У EmployeeService interface:
    Employee findEmployeeEntityByEmail(String email);









    Optional<UserEditDTO> findByIdForEdit(Long id);
    void updateEmployee(UserEditDTO userEditDTO);
    Optional<Employee> findById(Long id);

}