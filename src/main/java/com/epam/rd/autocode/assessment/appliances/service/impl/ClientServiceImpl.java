package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client create(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = repository.findById(id).orElseThrow();
        client.setId(id);
        return repository.save(client);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Client findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }
}