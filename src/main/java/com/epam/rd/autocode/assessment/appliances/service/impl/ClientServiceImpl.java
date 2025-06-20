package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public Client create(Client client) {
        return null;
    }

    @Override
    public Client update(Long id, Client client) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Client findById(Long id) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }
}