package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    // ✅ Метод для реєстрації користувача з хешуванням пароля і генерацією карти
    public void register(ClientRegisterDTO dto) {
        Client client = modelMapper.map(dto, Client.class);

        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setCard(generateCardNumber());

        clientRepository.save(client);
    }

    // 🧠 Генерація випадкового номера карти (16 цифр)
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}