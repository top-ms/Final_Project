package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.AddNewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.ViewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
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

@WebMvcTest(ManufacturersControllerForEmployee.class)
class ManufacturersControllerForEmployeeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManufacturerService manufacturerService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private ViewManufacturerAsDTO createTestManufacturerDTO() {
        ViewManufacturerAsDTO dto = new ViewManufacturerAsDTO();
        dto.setId(1L);
        dto.setName("Test Manufacturer");
        return dto;
    }

    private AddNewManufacturerAsDTO createTestAddManufacturerDTO() {
        AddNewManufacturerAsDTO dto = new AddNewManufacturerAsDTO();
        dto.setName("New Manufacturer");
        return dto;
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testViewListOfManufacturers_Success() throws Exception {
        ViewManufacturerAsDTO manufacturerDTO = createTestManufacturerDTO();
        Page<ViewManufacturerAsDTO> manufacturersPage = new PageImpl<>(List.of(manufacturerDTO));

        when(manufacturerService.getAllManufacturersAsDto(any(Pageable.class))).thenReturn(manufacturersPage);

        mockMvc.perform(get("/employee/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/manufacturers"))
                .andExpect(model().attributeExists("manufacturersPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testViewListOfManufacturers_WithPagination() throws Exception {
        ViewManufacturerAsDTO manufacturerDTO = createTestManufacturerDTO();
        Page<ViewManufacturerAsDTO> manufacturersPage = new PageImpl<>(List.of(manufacturerDTO));

        when(manufacturerService.getAllManufacturersAsDto(any(Pageable.class))).thenReturn(manufacturersPage);

        // When & Then
        mockMvc.perform(get("/employee/manufacturers")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/manufacturers"))
                .andExpect(model().attributeExists("manufacturersPage"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchManufacturers_Success() throws Exception {
        ViewManufacturerAsDTO manufacturerDTO = createTestManufacturerDTO();
        when(manufacturerService.getByName("Test Manufacturer")).thenReturn(Optional.of(manufacturerDTO));

        mockMvc.perform(get("/employee/manufacturers/search")
                        .param("name", "Test Manufacturer"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/manufacturers"))
                .andExpect(model().attributeExists("manufacturersPage"))
                .andExpect(model().attribute("notFound", false));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchManufacturers_NotFound() throws Exception {
        when(manufacturerService.getByName("NonExistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/employee/manufacturers/search")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/manufacturers"))
                .andExpect(model().attributeExists("manufacturersPage"))
                .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testSearchManufacturers_EmptyName() throws Exception {
        mockMvc.perform(get("/employee/manufacturers/search")
                        .param("name", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/manufacturers"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testShowFormForAddingNewManufacturer() throws Exception {
        mockMvc.perform(get("/employee/manufacturers/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/newManufacturer"))
                .andExpect(model().attributeExists("manufacturer"));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testAddNewManufacturer_Success() throws Exception {
        when(manufacturerService.existsByName("New Manufacturer")).thenReturn(false);

        mockMvc.perform(post("/employee/manufacturers/add-manufacturer")
                        .with(csrf())
                        .param("name", "New Manufacturer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/manufacturers"));

        verify(manufacturerService).saveNewManufacturer(any(AddNewManufacturerAsDTO.class));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testAddNewManufacturer_NameExists() throws Exception {
        when(manufacturerService.existsByName("Existing Manufacturer")).thenReturn(true);

        mockMvc.perform(post("/employee/manufacturers/add-manufacturer")
                        .with(csrf())
                        .param("name", "Existing Manufacturer"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/manufacture/newManufacturer"));

        verify(manufacturerService, never()).saveNewManufacturer(any(AddNewManufacturerAsDTO.class));
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testDeleteManufacturer_Success() throws Exception {
        mockMvc.perform(get("/employee/manufacturers/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/manufacturers?page=0"));

        verify(manufacturerService).deleteManufacturerById(1L);
    }

    @Test
    @WithMockUser(username = "employee@test.com", authorities = {"ROLE_EMPLOYEE"})
    void testDeleteManufacturer_WithPage() throws Exception {
        mockMvc.perform(get("/employee/manufacturers/1/delete")
                        .param("page", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/manufacturers?page=2"));

        verify(manufacturerService).deleteManufacturerById(1L);
    }
}