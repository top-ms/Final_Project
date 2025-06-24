package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    void addNewClient(Client client);
    void deleteClientById(Long id);
    void deleteAllOrdersOfClientById(Long id);
}