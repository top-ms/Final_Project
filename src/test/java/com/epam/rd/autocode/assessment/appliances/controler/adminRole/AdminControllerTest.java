package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.service.AdminService;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private ViewAdminDTO createTestAdminDTO() {
        ViewAdminDTO dto = new ViewAdminDTO();
        dto.setId(1L);
        dto.setName("Test Admin");
        dto.setEmail("admin@test.com");
        return dto;
    }

    private Admin createTestAdmin() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Test Admin");
        admin.setEmail("admin@test.com");
        return admin;
    }

    private RegistrationAdminDTO createTestRegistrationAdminDTO() {
        RegistrationAdminDTO dto = new RegistrationAdminDTO();
        dto.setName("New Admin");
        dto.setEmail("newadmin@test.com");
        dto.setPassword("password123");
        return dto;
    }

    private UserEditDTO createTestUserEditDTO() {
        UserEditDTO dto = new UserEditDTO();
        dto.setId(1L);
        dto.setName("Updated Admin");
        dto.setEmail("updated@test.com");
        return dto;
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfAdmins_Success() throws Exception {
        ViewAdminDTO adminDTO = createTestAdminDTO();
        Page<ViewAdminDTO> adminsPage = new PageImpl<>(List.of(adminDTO));

        when(adminService.getAllAdminsAsDto(any(Pageable.class))).thenReturn(adminsPage);

        mockMvc.perform(get("/admin/admins"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/admins"))
                .andExpect(model().attributeExists("adminsPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfAdmins_WithPagination() throws Exception {
        ViewAdminDTO adminDTO = createTestAdminDTO();
        Page<ViewAdminDTO> adminsPage = new PageImpl<>(List.of(adminDTO));

        when(adminService.getAllAdminsAsDto(any(Pageable.class))).thenReturn(adminsPage);

        mockMvc.perform(get("/admin/admins")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/admins"))
                .andExpect(model().attributeExists("adminsPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchAdmins_NotFound() throws Exception {
        when(adminService.getAdminByEmail("notfound@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/admins/search")
                        .param("email", "notfound@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/admins"))
                .andExpect(model().attributeExists("notFound"))
                .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchAdmins_EmptyEmail() throws Exception {
        mockMvc.perform(get("/admin/admins/search")
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteAdmin_Success() throws Exception {
        mockMvc.perform(get("/admin/admins/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins?page=0"));

        verify(adminService).deleteAdminById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteAdmin_WithPage() throws Exception {
        mockMvc.perform(get("/admin/admins/1/delete")
                        .param("page", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins?page=2"));

        verify(adminService).deleteAdminById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowFormForAddingAdmin() throws Exception {
        mockMvc.perform(get("/admin/admins/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/newAdmin"))
                .andExpect(model().attributeExists("admin"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewAdmin_Success() throws Exception {
        when(adminService.existsByEmail("newadmin@test.com")).thenReturn(false);

        mockMvc.perform(post("/admin/admins/add-admin")
                        .with(csrf())
                        .param("name", "New Admin")
                        .param("email", "newadmin@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins"));

        verify(adminService).register(any(RegistrationAdminDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewAdmin_EmailExists() throws Exception {
        when(adminService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/admins/add-admin")
                        .with(csrf())
                        .param("name", "New Admin")
                        .param("email", "existing@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/newAdmin"));

        verify(adminService, never()).register(any(RegistrationAdminDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditAdminForm_Success() throws Exception {
        UserEditDTO userEditDTO = createTestUserEditDTO();
        when(adminService.findByIdForEdit(1L)).thenReturn(Optional.of(userEditDTO));

        mockMvc.perform(get("/admin/admins/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/editAdmin"))
                .andExpect(model().attributeExists("admin"))
                .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditAdminForm_AdminNotFound() throws Exception {
        when(adminService.findByIdForEdit(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/admins/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateAdmin_AdminNotFound() throws Exception {
        when(adminService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/admins/1/update")
                        .with(csrf())
                        .param("name", "Updated Admin")
                        .param("email", "updated@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/admins"));

        verify(adminService, never()).updateAdmin(any(UserEditDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateAdmin_EmailExists() throws Exception {
        Admin existingAdmin = createTestAdmin();
        existingAdmin.setEmail("old@test.com");
        when(adminService.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(adminService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/admins/1/update")
                        .with(csrf())
                        .param("name", "Updated Admin")
                        .param("email", "existing@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin/editAdmin"))
                .andExpect(model().attribute("isEdit", true));

        verify(adminService, never()).updateAdmin(any(UserEditDTO.class));
    }
}