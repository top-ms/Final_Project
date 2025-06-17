package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientRepositoryTest {

    /**
     * Tests for the findByName method in the ClientRepository class.
     * The findByName method is used to retrieve a list of clients with a specified name.
     * These tests verify different scenarios when finding clients by their name.
     */
    @Test
    void testFindByNameReturnsSpecificClientEarth() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        String nameToSearch = "Earth";

        Client expectedClient = new Client(null, "Earth", "earth@gmail.com", "333", "5326-3333");
        List<Client> expectedClients = Collections.singletonList(expectedClient);
        when(clientRepository.findByName(nameToSearch)).thenReturn(expectedClients);

        // Act
        List<Client> result = clientRepository.findByName(nameToSearch);

        // Assert
        assertEquals(expectedClients, result, "Expected one client with the name 'Earth' to be returned.");
        verify(clientRepository, times(1)).findByName(nameToSearch);
    }

    /**
     * Tests for the findByEmail method in the ClientRepository class.
     * The findByEmail method is used to retrieve a list of clients with a given email.
     * These tests verify different scenarios when finding clients by their email.
     */
    @Test
    void testFindByEmailReturnsSpecificClient() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        String emailToSearch = "earth@gmail.com";

        Client expectedClient = new Client(null, "Earth", "earth@gmail.com", "333", "5326-3333");
        List<Client> expectedClients = Collections.singletonList(expectedClient);
        when(clientRepository.findByEmail(emailToSearch)).thenReturn(expectedClients);

        // Act
        List<Client> result = clientRepository.findByEmail(emailToSearch);

        // Assert
        assertEquals(expectedClients, result, "Expected one client with the email 'earth@gmail.com' to be returned.");
        verify(clientRepository, times(1)).findByEmail(emailToSearch);
    }

    @Test
    void testFindByEmailReturnsEmptyListWhenNoClientFound() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        String emailToSearch = "nonexistent@gmail.com";

        when(clientRepository.findByEmail(emailToSearch)).thenReturn(Collections.emptyList());

        // Act
        List<Client> result = clientRepository.findByEmail(emailToSearch);

        // Assert
        assertEquals(Collections.emptyList(), result, "Expected no clients to be found for the email 'nonexistent@gmail.com'.");
        verify(clientRepository, times(1)).findByEmail(emailToSearch);
    }

    /**
     * Tests for the findByCard method in the ClientRepository class.
     * These tests verify different scenarios when finding clients by their card.
     */
    @Test
    void testFindByCardReturnsSpecificClient() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        String cardToSearch = "5326-3333";

        Client expectedClient = new Client(null, "Earth", "earth@gmail.com", "333", "5326-3333");
        List<Client> expectedClients = Collections.singletonList(expectedClient);
        when(clientRepository.findByCard(cardToSearch)).thenReturn(expectedClients);

        // Act
        List<Client> result = clientRepository.findByCard(cardToSearch);

        // Assert
        assertEquals(expectedClients, result, "Expected one client with the card '5326-3333' to be returned.");
        verify(clientRepository, times(1)).findByCard(cardToSearch);
    }

    @Test
    void testFindByCardReturnsEmptyListWhenNoClientFound() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        String cardToSearch = "0000-0000";

        when(clientRepository.findByCard(cardToSearch)).thenReturn(Collections.emptyList());

        // Act
        List<Client> result = clientRepository.findByCard(cardToSearch);

        // Assert
        assertEquals(Collections.emptyList(), result, "Expected no clients to be found for the card '0000-0000'.");
        verify(clientRepository, times(1)).findByCard(cardToSearch);
    }


    @Test
    void testFindAllReturnsAllClients() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);

        Client client1 = new Client(1L, "Mercury", "mercury@gmail.com", "111", "5326-1111");
        Client client2 = new Client(2L, "Venus", "venus@gmail.com", "222", "5326-2222");
        List<Client> expectedClients = List.of(client1, client2);
        when(clientRepository.findAll()).thenReturn(expectedClients);

        // Act
        List<Client> result = clientRepository.findAll();

        // Assert
        assertEquals(expectedClients, result, "Expected all clients to be returned.");
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdReturnsSpecificClient() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        Long idToSearch = 1L;

        Client expectedClient = new Client(1L, "Mercury", "mercury@gmail.com", "111", "5326-1111");
        when(clientRepository.findById(idToSearch)).thenReturn(java.util.Optional.of(expectedClient));

        // Act
        Client result = clientRepository.findById(idToSearch).orElse(null);

        // Assert
        assertEquals(expectedClient, result, "Expected the client with ID 1 to be returned.");
        verify(clientRepository, times(1)).findById(idToSearch);
    }

    @Test
    void testSavePersistsClient() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        Client clientToSave = new Client(null, "Pluto", "pluto@gmail.com", "444", "5326-9999");
        Client savedClient = new Client(9L, "Pluto", "pluto@gmail.com", "444", "5326-9999");

        when(clientRepository.save(clientToSave)).thenReturn(savedClient);

        // Act
        Client result = clientRepository.save(clientToSave);

        // Assert
        assertEquals(savedClient, result, "Expected the client to be saved and returned.");
        verify(clientRepository, times(1)).save(clientToSave);
    }

    @Test
    void testDeleteByIdRemovesClient() {
        // Arrange
        ClientRepository clientRepository = mock(ClientRepository.class);
        Long idToDelete = 1L;

        doNothing().when(clientRepository).deleteById(idToDelete);

        // Act
        clientRepository.deleteById(idToDelete);

        // Assert
        verify(clientRepository, times(1)).deleteById(idToDelete);
    }

    //-------------------------------------------


}