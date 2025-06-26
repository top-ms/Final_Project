package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversalUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;

    public UniversalUserDetailsService(ClientRepository clientRepository, EmployeeRepository employeeRepository, AdminRepository adminRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Перевіряємо в адмінах
        var adminOpt = adminRepository.findByEmail(username);
        if (adminOpt.isPresent()) {
            var admin = adminOpt.get();
            System.out.println("Admin found: " + admin.getEmail() + " with password: " + admin.getPassword() + "");
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // 2. Перевіряємо в працівниках
        var employeeOpt = employeeRepository.findByEmail(username);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            System.out.println("Employee found: " + employee.getEmail() + " with password: " + employee.getPassword() + "");
            return new User(
                    employee.getEmail(),
                    employee.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
            );
        }

        // 3. Перевіряємо в клієнтах
        var clientOpt = clientRepository.findByEmail(username);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client found: " + client.getEmail() + " with password: " + client.getPassword() + "");
            return new User(
                    client.getEmail(),
                    client.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
            );
        }

        // 4. Якщо не знайдено — кидаємо помилку
        throw new UsernameNotFoundException("User with email '" + username + "' not found");
    }
}
