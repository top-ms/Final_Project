package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrdersRepository ordersRepository;

    public ClientServiceImpl(ClientRepository clientRepository, OrdersRepository ordersRepository) {
        this.clientRepository = clientRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void addNewClient(Client client) {
        clientRepository.save(client);
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

}