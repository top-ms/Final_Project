package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
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

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private ViewClientsByAdminDTO createTestClientDTO() {
        ViewClientsByAdminDTO dto = new ViewClientsByAdminDTO();
        dto.setId(1L);
        dto.setName("Test Client");
        dto.setEmail("client@test.com");
        return dto;
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("client@test.com");
        return client;
    }

    private ClientRegisterDTO createTestClientRegisterDTO() {
        ClientRegisterDTO dto = new ClientRegisterDTO();
        dto.setName("New Client");
        dto.setEmail("newclient@test.com");
        dto.setPassword("password123");
        return dto;
    }

    private UserEditDTO createTestUserEditDTO() {
        UserEditDTO dto = new UserEditDTO();
        dto.setId(1L);
        dto.setName("Updated Client");
        dto.setEmail("updated@test.com");
        return dto;
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfClients_Success() throws Exception {
        ViewClientsByAdminDTO clientDTO = createTestClientDTO();
        Page<ViewClientsByAdminDTO> clientsPage = new PageImpl<>(List.of(clientDTO));

        when(clientService.getAllClientsAsDto(any(Pageable.class))).thenReturn(clientsPage);
        when(clientService.getAllClients()).thenReturn(List.of(createTestClient()));

        mockMvc.perform(get("/admin/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/clients"))
                .andExpect(model().attributeExists("clientsPage"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testViewListOfClients_WithPagination() throws Exception {
        ViewClientsByAdminDTO clientDTO = createTestClientDTO();
        Page<ViewClientsByAdminDTO> clientsPage = new PageImpl<>(List.of(clientDTO));

        when(clientService.getAllClientsAsDto(any(Pageable.class))).thenReturn(clientsPage);
        when(clientService.getAllClients()).thenReturn(List.of(createTestClient()));

        mockMvc.perform(get("/admin/clients")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/clients"))
                .andExpect(model().attributeExists("clientsPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchClients_Success() throws Exception {
        ViewClientsByAdminDTO clientDTO = createTestClientDTO();
        when(clientService.findByEmail("client@test.com")).thenReturn(Optional.of(clientDTO));

        mockMvc.perform(get("/admin/clients/search")
                        .param("email", "client@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/clients"))
                .andExpect(model().attributeExists("clientsPage"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchClients_NotFound() throws Exception {
        when(clientService.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/clients/search")
                        .param("email", "notfound@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/clients"))
                .andExpect(model().attributeExists("notFound"))
                .andExpect(model().attribute("notFound", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testSearchClients_EmptyEmail() throws Exception {
        mockMvc.perform(get("/admin/clients/search")
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowFormForAddingClient() throws Exception {
        mockMvc.perform(get("/admin/clients/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/newClient"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testRegisterClient_Success() throws Exception {
        when(clientService.existsByEmail("newclient@test.com")).thenReturn(false);

        mockMvc.perform(post("/admin/register")
                        .with(csrf())
                        .param("name", "New Client")
                        .param("email", "newclient@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(clientService).register(any(ClientRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testRegisterClient_EmailExists() throws Exception {
        when(clientService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/register")
                        .with(csrf())
                        .param("name", "New Client")
                        .param("email", "existing@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("entrance/register"));

        verify(clientService, never()).register(any(ClientRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewClient_Success() throws Exception {
        when(clientService.existsByEmail("newclient@test.com")).thenReturn(false);

        mockMvc.perform(post("/admin/clients/add-client")
                        .with(csrf())
                        .param("name", "New Client")
                        .param("email", "newclient@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients"));

        verify(clientService).register(any(ClientRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testAddNewClient_EmailExists() throws Exception {
        when(clientService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/clients/add-client")
                        .with(csrf())
                        .param("name", "New Client")
                        .param("email", "existing@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/newClient"));

        verify(clientService, never()).register(any(ClientRegisterDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteClient_Success() throws Exception {
        mockMvc.perform(get("/admin/clients/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients?page=0"));

        verify(clientService).deleteClientById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testDeleteClient_WithPage() throws Exception {
        mockMvc.perform(get("/admin/clients/1/delete")
                        .param("page", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients?page=2"));

        verify(clientService).deleteClientById(1L);
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditClientForm_Success() throws Exception {
        UserEditDTO userEditDTO = createTestUserEditDTO();
        when(clientService.findByIdForEdit(1L)).thenReturn(Optional.of(userEditDTO));

        mockMvc.perform(get("/admin/clients/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/editClient"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testShowEditClientForm_ClientNotFound() throws Exception {
        when(clientService.findByIdForEdit(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/clients/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateClient_ClientNotFound() throws Exception {
        when(clientService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/clients/1/update")
                        .with(csrf())
                        .param("name", "Updated Client")
                        .param("email", "updated@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/clients"));

        verify(clientService, never()).updateClient(any(UserEditDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ROLE_ADMIN"})
    void testUpdateClient_EmailExists() throws Exception {
        Client existingClient = createTestClient();
        existingClient.setEmail("old@test.com");
        when(clientService.findById(1L)).thenReturn(Optional.of(existingClient));
        when(clientService.existsByEmail("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/admin/clients/1/update")
                        .with(csrf())
                        .param("name", "Updated Client")
                        .param("email", "existing@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/client/editClient"))
                .andExpect(model().attribute("isEdit", true));

        verify(clientService, never()).updateClient(any(UserEditDTO.class));
    }
}