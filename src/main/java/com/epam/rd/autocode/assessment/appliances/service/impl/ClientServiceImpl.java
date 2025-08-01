package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrdersRepository ordersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public ClientServiceImpl(ClientRepository clientRepository,
                             OrdersRepository ordersRepository,
                             BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.ordersRepository = ordersRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<ViewClientsByAdminDTO> findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .map(client -> modelMapper.map(client, ViewClientsByAdminDTO.class));
    }
    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }
    @Override
    public Page<ViewClientsByAdminDTO> getAllClientsAsDto(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(client -> modelMapper.map(client, ViewClientsByAdminDTO.class));
    }

    @Override
    public Client findClientEntityByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + email));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteClientById(Long id) {
        deleteAllOrdersOfClientById(id);
        clientRepository.deleteById(id);
    }

    @Override
    public void deleteAllOrdersOfClientById(Long id){
        List<Orders> orders = ordersRepository.findAllByClientId(id);
        ordersRepository.deleteAll(orders);
    }

    public void register(ClientRegisterDTO dto) {
        Client client = modelMapper.map(dto, Client.class);

        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setCard(generateCardNumber());

        clientRepository.save(client);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public Optional<UserEditDTO> findByIdForEdit(Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    UserEditDTO dto = new UserEditDTO();
                    dto.setId(client.getId());
                    dto.setName(client.getName());
                    dto.setEmail(client.getEmail());
                    dto.setPassword("");
                    return dto;
                });
    }

    @Override
    public void updateClient(UserEditDTO userEditDTO) {
        Client client = clientRepository.findById(userEditDTO.getId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + userEditDTO.getId()));

        client.setName(userEditDTO.getName());
        client.setEmail(userEditDTO.getEmail());

        // Хешуємо новий пароль
        client.setPassword(passwordEncoder.encode(userEditDTO.getPassword()));

        clientRepository.save(client);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public boolean updateClientPassword(Long clientId, String encodedPassword) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(clientId);
            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();
                client.setPassword(encodedPassword);
                clientRepository.save(client);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}