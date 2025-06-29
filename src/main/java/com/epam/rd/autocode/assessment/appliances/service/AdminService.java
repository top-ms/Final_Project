package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<Admin> getAllAdmins();
    void register(Admin admin);
    void deleteAdminById(Long id);
}
