package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.service.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(Admin admin) {
        admin.setEmail("k@gmail.com");
        admin.setPassword(passwordEncoder.encode("1111"));
        adminRepository.save(admin);
        System.out.println("Registered admin: " + admin.getEmail() + " with password: " + admin.getPassword() + "");
    }
}
