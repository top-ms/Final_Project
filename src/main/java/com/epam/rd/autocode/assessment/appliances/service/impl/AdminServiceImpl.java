package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Page<ViewAdminDTO> getAllAdminsAsDto(Pageable pageable) {
        return adminRepository.findAll(pageable)
                .map(admin -> modelMapper.map(admin, ViewAdminDTO.class)) ;
    }

    @Override
    public Optional<ViewAdminDTO> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(admin -> modelMapper.map(admin, ViewAdminDTO.class));
    }

    @Override
    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }






    @Override
    public void register(RegistrationAdminDTO registrationAdminDTO) {
        Admin admin = modelMapper.map(registrationAdminDTO, Admin.class);
        admin.setPassword(passwordEncoder.encode(registrationAdminDTO.getPassword()));
        adminRepository.save(admin);
    }


    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }


}
