//package com.epam.rd.autocode.assessment.appliances.controller;
//
//import com.epam.rd.autocode.assessment.appliances.controler.adminRole.AdminController;
//import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
//import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
//import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
//import com.epam.rd.autocode.assessment.appliances.model.Admin;
//import com.epam.rd.autocode.assessment.appliances.service.AdminService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//// ✅ Правильно:
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AdminController.class)
//@DisplayName("AdminController Tests")
//class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AdminService adminService;
//
//    @Nested
//    @DisplayName("View List of Admins Tests")
//    class ViewListOfAdminsTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should display admins list successfully")
//        void shouldDisplayAdminsListSuccessfully() throws Exception {
//            // Given
//            ViewAdminDTO admin1 = new ViewAdminDTO(1L, "Admin1", "admin1@test.com");
//            ViewAdminDTO admin2 = new ViewAdminDTO(2L, "Admin2", "admin2@test.com");
//            List<ViewAdminDTO> adminsList = Arrays.asList(admin1, admin2);
//            Page<ViewAdminDTO> adminsPage = new PageImpl<>(adminsList);
//
//            when(adminService.getAllAdminsAsDto(any())).thenReturn(adminsPage);
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/admins"))
//                    .andExpect(model().attribute("adminsPage", adminsPage))
//                    .andExpect(model().attribute("currentPage", 0))
//                    .andExpect(model().attribute("totalPages", 1))
//                    .andExpect(model().attribute("sort", "asc"));
//
//            verify(adminService).getAllAdminsAsDto(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle pagination parameters correctly")
//        void shouldHandlePaginationParametersCorrectly() throws Exception {
//            // Given
//            Page<ViewAdminDTO> adminsPage = new PageImpl<>(Arrays.asList());
//            when(adminService.getAllAdminsAsDto(any())).thenReturn(adminsPage);
//
//            mockMvc.perform(get("/admin/admins")
//                            .param("page", "2")
//                            .param("size", "5"))
//                    .andExpect(status().isOk())
//                    .andExpect(model().attribute("currentPage", 2));  // ← andExpect, не andExpected
//
//            verify(adminService).getAllAdminsAsDto(PageRequest.of(2, 5, Sort.by(Sort.Direction.ASC, "name")));
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle ASC sorting correctly")
//        void shouldHandleAscSortingCorrectly() throws Exception {
//            // Given
//            Page<ViewAdminDTO> adminsPage = new PageImpl<>(Arrays.asList());
//            when(adminService.getAllAdminsAsDto(any())).thenReturn(adminsPage);
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins").param("sort", "asc"))
//                    .andExpect(status().isOk())
//                    .andExpect(model().attribute("sort", "asc"));
//
//            verify(adminService).getAllAdminsAsDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle DESC sorting correctly")
//        void shouldHandleDescSortingCorrectly() throws Exception {
//            // Given
//            Page<ViewAdminDTO> adminsPage = new PageImpl<>(Arrays.asList());
//            when(adminService.getAllAdminsAsDto(any())).thenReturn(adminsPage);
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins").param("sort", "desc"))
//                    .andExpect(status().isOk())
//                    .andExpect(model().attribute("sort", "desc"));
//
//            verify(adminService).getAllAdminsAsDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));
//        }
//    }
//
//    @Nested
//    @DisplayName("Search Admins Tests")
//    class SearchAdminsTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should find admin by email successfully")
//        void shouldFindAdminByEmailSuccessfully() throws Exception {
//            // Given
//            String email = "admin@test.com";
//            ViewAdminDTO admin = new ViewAdminDTO(1L, "Admin", email);
//            when(adminService.getAdminByEmail(email)).thenReturn(Optional.of(admin));
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/search").param("email", email))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/admins"))
//                    .andExpect(model().attribute("adminsPage", admin))
//                    .andExpect(model().attribute("currentPage", 0))
//                    .andExpect(model().attribute("totalPages", 1));
//
//            verify(adminService).getAdminByEmail(email);
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle admin not found")
//        void shouldHandleAdminNotFound() throws Exception {
//            // Given
//            String email = "nonexistent@test.com";
//            when(adminService.getAdminByEmail(email)).thenReturn(Optional.empty());
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/search").param("email", email))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/admins"))
//                    .andExpect(model().attribute("notFound", true))
//                    .andExpect(model().attribute("adminsPage", Page.empty()));
//
//            verify(adminService).getAdminByEmail(email);
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should redirect when email is empty")
//        void shouldRedirectWhenEmailIsEmpty() throws Exception {
//            // When & Then
//            mockMvc.perform(get("/admin/admins/search").param("email", ""))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService, never()).getAdminByEmail(anyString());
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete Admin Tests")
//    class DeleteAdminTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should delete admin successfully")
//        void shouldDeleteAdminSuccessfully() throws Exception {
//            // Given
//            Long adminId = 1L;
//            doNothing().when(adminService).deleteAdminById(adminId);
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/{id}/delete", adminId))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins?page=0"));
//
//            verify(adminService).deleteAdminById(adminId);
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should redirect with correct page parameter")
//        void shouldRedirectWithCorrectPageParameter() throws Exception {
//            // Given
//            Long adminId = 1L;
//            int page = 3;
//            doNothing().when(adminService).deleteAdminById(adminId);
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/{id}/delete", adminId).param("page", String.valueOf(page)))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins?page=" + page));
//
//            verify(adminService).deleteAdminById(adminId);
//        }
//    }
//
//    @Nested
//    @DisplayName("Show Form Tests")
//    class ShowFormTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should show new admin form")
//        void shouldShowNewAdminForm() throws Exception {
//            // When & Then
//            mockMvc.perform(get("/admin/admins/add"))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/newAdmin"))
//                    .andExpect(model().attributeExists("admin"))
//                    .andExpect(model().attribute("admin", any(RegistrationAdminDTO.class)));
//        }
//    }
//
//    @Nested
//    @DisplayName("Add New Admin Tests")
//    class AddNewAdminTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should create new admin successfully")
//        void shouldCreateNewAdminSuccessfully() throws Exception {
//            // Given
//            RegistrationAdminDTO dto = new RegistrationAdminDTO();
//            dto.setName("New Admin");
//            dto.setEmail("newadmin@test.com");
//            dto.setPassword("password123");
//
//            when(adminService.existsByEmail(dto.getEmail())).thenReturn(false);
//            doNothing().when(adminService).register(any(RegistrationAdminDTO.class));
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/add-admin")
//                            .with(csrf())
//                            .flashAttr("admin", dto))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService).existsByEmail(dto.getEmail());
//            verify(adminService).register(any(RegistrationAdminDTO.class));
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle email already exists error")
//        void shouldHandleEmailAlreadyExistsError() throws Exception {
//            // Given
//            RegistrationAdminDTO dto = new RegistrationAdminDTO();
//            dto.setName("New Admin");
//            dto.setEmail("existing@test.com");
//            dto.setPassword("password123");
//
//            when(adminService.existsByEmail(dto.getEmail())).thenReturn(true);
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/add-admin")
//                            .with(csrf())
//                            .flashAttr("admin", dto))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/newAdmin"))
//                    .andExpect(model().hasErrors())
//                    .andExpect(model().attributeHasFieldErrors("admin", "email"));
//
//            verify(adminService).existsByEmail(dto.getEmail());
//            verify(adminService, never()).register(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle validation errors")
//        void shouldHandleValidationErrors() throws Exception {
//            // Given - DTO з порожніми полями
//            RegistrationAdminDTO dto = new RegistrationAdminDTO();
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/add-admin")
//                            .with(csrf())
//                            .flashAttr("admin", dto))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/newAdmin"))
//                    .andExpect(model().hasErrors());
//
//            verify(adminService, never()).existsByEmail(anyString());
//            verify(adminService, never()).register(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should return form with errors and preserve data")
//        void shouldReturnFormWithErrorsAndPreserveData() throws Exception {
//            // Given
//            RegistrationAdminDTO dto = new RegistrationAdminDTO();
//            dto.setName(""); // порожнє ім'я для помилки валідації
//            dto.setEmail("invalid-email"); // неправильний email
//            dto.setPassword("123"); // короткий пароль
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/add-admin")
//                            .with(csrf())
//                            .flashAttr("admin", dto))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/newAdmin"))
//                    .andExpect(model().attribute("admin", dto))
//                    .andExpect(model().hasErrors());
//        }
//    }
//
//    @Nested
//    @DisplayName("Show Edit Form Tests")
//    class ShowEditFormTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should show edit form for existing admin")
//        void shouldShowEditFormForExistingAdmin() throws Exception {
//            // Given
//            Long adminId = 1L;
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setId(adminId);
//            editDTO.setName("Admin");
//            editDTO.setEmail("admin@test.com");
//
//            when(adminService.findByIdForEdit(adminId)).thenReturn(Optional.of(editDTO));
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/{id}/edit", adminId))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/editAdmin"))
//                    .andExpect(model().attribute("admin", editDTO))
//                    .andExpect(model().attribute("isEdit", true));
//
//            verify(adminService).findByIdForEdit(adminId);
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should redirect when admin not found")
//        void shouldRedirectWhenAdminNotFound() throws Exception {
//            // Given
//            Long adminId = 999L;
//            when(adminService.findByIdForEdit(adminId)).thenReturn(Optional.empty());
//
//            // When & Then
//            mockMvc.perform(get("/admin/admins/{id}/edit", adminId))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService).findByIdForEdit(adminId);
//        }
//    }
//
//    @Nested
//    @DisplayName("Update Admin Tests")
//    class UpdateAdminTests {
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should update admin successfully")
//        void shouldUpdateAdminSuccessfully() throws Exception {
//            // Given
//            Long adminId = 1L;
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setId(adminId);
//            editDTO.setName("Updated Admin");
//            editDTO.setEmail("updated@test.com");
//            editDTO.setPassword("newpassword123");
//
//            Admin existingAdmin = new Admin();
//            existingAdmin.setId(adminId);
//            existingAdmin.setEmail("old@test.com");
//
//            when(adminService.findById(adminId)).thenReturn(Optional.of(existingAdmin));
//            when(adminService.existsByEmail(editDTO.getEmail())).thenReturn(false);
//            doNothing().when(adminService).updateAdmin(any(UserEditDTO.class));
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/{id}/update", adminId)
//                            .with(csrf())
//                            .flashAttr("admin", editDTO))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService).findById(adminId);
//            verify(adminService).existsByEmail(editDTO.getEmail());
//            verify(adminService).updateAdmin(any(UserEditDTO.class));
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should redirect when admin not found")
//        void shouldRedirectWhenAdminNotFoundOnUpdate() throws Exception {
//            // Given
//            Long adminId = 999L;
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setName("Admin");
//            editDTO.setEmail("admin@test.com");
//            editDTO.setPassword("password123");
//
//            when(adminService.findById(adminId)).thenReturn(Optional.empty());
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/{id}/update", adminId)
//                            .with(csrf())
//                            .flashAttr("admin", editDTO))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService).findById(adminId);
//            verify(adminService, never()).updateAdmin(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle email already taken by another admin")
//        void shouldHandleEmailAlreadyTakenByAnotherAdmin() throws Exception {
//            // Given
//            Long adminId = 1L;
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setName("Admin");
//            editDTO.setEmail("taken@test.com");
//            editDTO.setPassword("password123");
//
//            Admin existingAdmin = new Admin();
//            existingAdmin.setId(adminId);
//            existingAdmin.setEmail("old@test.com");
//
//            when(adminService.findById(adminId)).thenReturn(Optional.of(existingAdmin));
//            when(adminService.existsByEmail(editDTO.getEmail())).thenReturn(true);
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/{id}/update", adminId)
//                            .with(csrf())
//                            .flashAttr("admin", editDTO))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/editAdmin"))
//                    .andExpect(model().hasErrors())
//                    .andExpect(model().attributeHasFieldErrors("admin", "email"))
//                    .andExpect(model().attribute("isEdit", true));
//
//            verify(adminService, never()).updateAdmin(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should handle validation errors on update")
//        void shouldHandleValidationErrorsOnUpdate() throws Exception {
//            // Given
//            Long adminId = 1L;
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setName(""); // порожнє ім'я
//            editDTO.setEmail("invalid-email");
//            editDTO.setPassword("");
//
//            Admin existingAdmin = new Admin();
//            existingAdmin.setId(adminId);
//            existingAdmin.setEmail("admin@test.com");
//
//            when(adminService.findById(adminId)).thenReturn(Optional.of(existingAdmin));
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/{id}/update", adminId)
//                            .with(csrf())
//                            .flashAttr("admin", editDTO))
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("admin/admin/editAdmin"))
//                    .andExpect(model().hasErrors())
//                    .andExpect(model().attribute("isEdit", true));
//
//            verify(adminService, never()).updateAdmin(any());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_ADMIN")
//        @DisplayName("Should allow keeping same email")
//        void shouldAllowKeepingSameEmail() throws Exception {
//            // Given
//            Long adminId = 1L;
//            String sameEmail = "admin@test.com";
//            UserEditDTO editDTO = new UserEditDTO();
//            editDTO.setName("Updated Admin");
//            editDTO.setEmail(sameEmail);
//            editDTO.setPassword("newpassword123");
//
//            Admin existingAdmin = new Admin();
//            existingAdmin.setId(adminId);
//            existingAdmin.setEmail(sameEmail); // той самий email
//
//            when(adminService.findById(adminId)).thenReturn(Optional.of(existingAdmin));
//            doNothing().when(adminService).updateAdmin(any(UserEditDTO.class));
//
//            // When & Then
//            mockMvc.perform(post("/admin/admins/{id}/update", adminId)
//                            .with(csrf())
//                            .flashAttr("admin", editDTO))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/admin/admins"));
//
//            verify(adminService).findById(adminId);
//            // Не перевіряємо existsByEmail, бо email не змінився
//            verify(adminService, never()).existsByEmail(anyString());
//            verify(adminService).updateAdmin(any(UserEditDTO.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("Security Tests")
//    class SecurityTests {
//
//        @Test
//        @DisplayName("Should deny access without authentication")
//        void shouldDenyAccessWithoutAuthentication() throws Exception {
//            mockMvc.perform(get("/admin/admins"))
//                    .andExpect(status().isUnauthorized());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_USER")
//        @DisplayName("Should deny access with wrong role")
//        void shouldDenyAccessWithWrongRole() throws Exception {
//            mockMvc.perform(get("/admin/admins"))
//                    .andExpect(status().isForbidden());
//        }
//
//        @Test
//        @WithMockUser(authorities = "ROLE_EMPLOYEE")
//        @DisplayName("Should deny access for employee role")
//        void shouldDenyAccessForEmployeeRole() throws Exception {
//            mockMvc.perform(get("/admin/admins"))
//                    .andExpect(status().isForbidden());
//        }
//    }
//}
