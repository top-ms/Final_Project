package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void testRegister() {
        RegistrationAdminDTO dto = new RegistrationAdminDTO("Test", "test@example.com", "password");
        Admin admin = new Admin();
        admin.setEmail("test@example.com");

        when(modelMapper.map(dto, Admin.class)).thenReturn(admin);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        adminService.register(dto);

        verify(adminRepository).save(argThat(savedAdmin ->
                savedAdmin.getEmail().equals(dto.getEmail()) &&
                        savedAdmin.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    void testExistsByEmail() {
        String email = "test@example.com";
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(new Admin()));

        assertTrue(adminService.existsByEmail(email));
    }

    @Test
    void testFindByIdForEdit() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Test");
        admin.setEmail("test@example.com");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<UserEditDTO> result = adminService.findByIdForEdit(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
        assertEquals("test@example.com", result.get().getEmail());
        assertEquals("", result.get().getPassword());
    }

    @Test
    void testUpdateAdmin() {
        UserEditDTO dto = new UserEditDTO();
        dto.setId(1L);
        dto.setName("Updated");
        dto.setEmail("updated@example.com");
        dto.setPassword("newpassword");

        Admin existingAdmin = new Admin();
        existingAdmin.setId(1L);
        existingAdmin.setName("Old");
        existingAdmin.setEmail("old@example.com");
        existingAdmin.setPassword("oldpassword");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNew");

        adminService.updateAdmin(dto);

        assertEquals("Updated", existingAdmin.getName());
        assertEquals("updated@example.com", existingAdmin.getEmail());
        assertEquals("encodedNew", existingAdmin.getPassword());
        verify(adminRepository).save(existingAdmin);
    }

    @Test
    void testGetAllAdminsAsDto() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Test");
        admin.setEmail("test@example.com");

        ViewAdminDTO dto = new ViewAdminDTO(1L, "Test", "test@example.com");

        Page<Admin> adminPage = new PageImpl<>(List.of(admin));
        when(adminRepository.findAll(any(PageRequest.class))).thenReturn(adminPage);
        when(modelMapper.map(admin, ViewAdminDTO.class)).thenReturn(dto);

        Page<ViewAdminDTO> result = adminService.getAllAdminsAsDto(PageRequest.of(0, 5));
        assertEquals(1, result.getTotalElements());
        assertEquals("Test", result.getContent().get(0).getName());
    }
}
