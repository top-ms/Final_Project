package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private ClientRepository clientRepository;
    private OrdersRepository ordersRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        ordersRepository = mock(OrdersRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        modelMapper = mock(ModelMapper.class);

        clientService = new ClientServiceImpl(clientRepository, ordersRepository, passwordEncoder, modelMapper);
    }

    @Test
    void testRegister() {
        ClientRegisterDTO dto = new ClientRegisterDTO("John", "john@example.com", "password");
        Client client = new Client();
        when(modelMapper.map(dto, Client.class)).thenReturn(client);
        when(passwordEncoder.encode("password")).thenReturn("encoded");

        clientService.register(dto);

        assertEquals("encoded", client.getPassword());
        assertNotNull(client.getCard());
        verify(clientRepository).save(client);
    }

    @Test
    void testFindByEmail() {
        Client client = new Client();
        ViewClientsByAdminDTO dto = new ViewClientsByAdminDTO();
        when(clientRepository.findByEmail("john@example.com")).thenReturn(Optional.of(client));
        when(modelMapper.map(client, ViewClientsByAdminDTO.class)).thenReturn(dto);

        Optional<ViewClientsByAdminDTO> result = clientService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
    }

    @Test
    void testExistsByEmail() {
        when(clientRepository.findByEmail("a@b.com")).thenReturn(Optional.of(new Client()));
        assertTrue(clientService.existsByEmail("a@b.com"));
    }

    @Test
    void testDeleteClientById() {
        Orders order = new Orders();
        when(ordersRepository.findAllByClientId(1L)).thenReturn(List.of(order));

        clientService.deleteClientById(1L);

        verify(ordersRepository).deleteAll(List.of(order));
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void testUpdateClient() {
        UserEditDTO dto = new UserEditDTO();
        dto.setId(1L);
        dto.setName("Updated");
        dto.setEmail("upd@example.com");
        dto.setPassword("newpass");

        Client client = new Client();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");

        clientService.updateClient(dto);

        assertEquals("Updated", client.getName());
        assertEquals("upd@example.com", client.getEmail());
        assertEquals("encodedPass", client.getPassword());
        verify(clientRepository).save(client);
    }

    @Test
    void testFindByIdForEdit() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test");
        client.setEmail("test@test.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<UserEditDTO> result = clientService.findByIdForEdit(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
        assertEquals("test@test.com", result.get().getEmail());
        assertEquals("", result.get().getPassword());
    }
}

