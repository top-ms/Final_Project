package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Page<ViewAdminDTO> getAllAdminsAsDto(Pageable pageable);

    Optional<ViewAdminDTO> getAdminByEmail(String email);

    void deleteAdminById(Long id);

    void register(RegistrationAdminDTO registrationAdminDTO);

    boolean existsByEmail(String email);








    // Методи для редагування
    Optional<UserEditDTO> findByIdForEdit(Long id);
    void updateAdmin(UserEditDTO userEditDTO);
    Optional<Admin> findById(Long id);
}
