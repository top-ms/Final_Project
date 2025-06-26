package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
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

    public ClientServiceImpl(ClientRepository clientRepository,
                             OrdersRepository ordersRepository,
                             BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.ordersRepository = ordersRepository;
        this.passwordEncoder = passwordEncoder;
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

//    @Override
//    public Optional<Client> findByEmail(String email){
//        return clientRepository.findByEmail(email);
//    }

    // 🔐 Додаємо реалізацію UserDetailsService



    // ✅ Метод для реєстрації користувача з хешуванням пароля і генерацією карти
    public void register(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setCard(generateCardNumber()); // 🧠 Генеруємо випадковий номер
        clientRepository.save(client);
        System.out.println("Registered client: " + client.getEmail() + " with card: " + client.getPassword() + "");
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