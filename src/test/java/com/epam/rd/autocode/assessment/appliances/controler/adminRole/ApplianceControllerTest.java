package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplianceController.class)
class ApplianceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplianceService applianceService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private ViewApplianceDTO createTestApplianceDTO() {
        ViewApplianceDTO dto = new ViewApplianceDTO();
        dto.setId(1L);
        dto.setName("Test Appliance");
        dto.setPrice(BigDecimal.valueOf(100.0));
        return dto;
    }

    private Appliance createTestAppliance() {
        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Test Appliance");
        appliance.setPrice(BigDecimal.valueOf(100.0));
        return appliance;
    }

    private Manufacturer createTestManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Test Manufacturer");
        return manufacturer;
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfAppliances_Success() throws Exception {
        ViewApplianceDTO applianceDTO = createTestApplianceDTO();
        Page<ViewApplianceDTO> appliancesPage = new PageImpl<>(List.of(applianceDTO));

        when(applianceService.getAllManufacturersAsDto(any(Pageable.class))).thenReturn(appliancesPage);

        mockMvc.perform(get("/admin/appliances"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/appliance/appliances"))
                .andExpect(model().attributeExists("appliancesPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfAppliances_WithPagination() throws Exception {
        ViewApplianceDTO applianceDTO = createTestApplianceDTO();
        Page<ViewApplianceDTO> appliancesPage = new PageImpl<>(List.of(applianceDTO));

        when(applianceService.getAllManufacturersAsDto(any(Pageable.class))).thenReturn(appliancesPage);

        mockMvc.perform(get("/admin/appliances")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/appliance/appliances"))
                .andExpect(model().attributeExists("appliancesPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchAppliances_NotFound() throws Exception {
        when(applianceService.getByName("NonExistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/appliances/search")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/appliance/appliances"))
                .andExpect(model().attributeExists("notFound"))
                .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchAppliances_EmptyName() throws Exception {
        mockMvc.perform(get("/admin/appliances/search")
                        .param("name", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appliances"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteAppliance_Success() throws Exception {
        mockMvc.perform(get("/admin/appliances/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appliances?page=0"));

        verify(applianceService).deleteApplianceById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteAppliance_WithPage() throws Exception {
        mockMvc.perform(get("/admin/appliances/1/delete")
                        .param("page", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appliances?page=2"));

        verify(applianceService).deleteApplianceById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowNewApplianceForm() throws Exception {
        Manufacturer manufacturer = createTestManufacturer();
        when(applianceService.getAllManufacturers()).thenReturn(List.of(manufacturer));

        mockMvc.perform(get("/admin/appliances/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/appliance/newAppliance"))
                .andExpect(model().attributeExists("appliance"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("powerTypes"))
                .andExpect(model().attributeExists("manufacturers"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewAppliance_Success() throws Exception {
        mockMvc.perform(post("/admin/appliances/add-appliance")
                        .with(csrf())
                        .param("name", "New Appliance")
                        .param("price", "150.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appliances"));

        verify(applianceService).addNewAppliance(any(ViewApplianceDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditForm_Success() throws Exception {
        ViewApplianceDTO applianceDTO = createTestApplianceDTO();
        Manufacturer manufacturer = createTestManufacturer();

        when(applianceService.getApplianceById(1L)).thenReturn(Optional.of(applianceDTO));
        when(applianceService.getAllManufacturers()).thenReturn(List.of(manufacturer));

        mockMvc.perform(get("/admin/appliances/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/appliance/editAppliance"))
                .andExpect(model().attributeExists("appliance"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("powerTypes"))
                .andExpect(model().attributeExists("manufacturers"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateAppliance_Success() throws Exception {
        mockMvc.perform(post("/admin/appliances/1/update-appliance")
                        .with(csrf())
                        .param("name", "Updated Appliance")
                        .param("price", "200.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appliances"));

        verify(applianceService).updateAppliance(eq(1L), any(ViewApplianceDTO.class));
    }
}