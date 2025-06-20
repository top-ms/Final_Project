package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;

import java.util.List;

public interface ClientService {
    Client create(Client client);
    Client update(Long id, Client client);
    void delete(Long id);
    Client findById(Long id);
    List<Client> findAll();
}