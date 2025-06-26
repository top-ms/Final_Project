package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    void addNewClient(Client client);
    void deleteClientById(Long id);
    void deleteAllOrdersOfClientById(Long id);
    Client findByEmail(String email);

    void register(Client client);
}