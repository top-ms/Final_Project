package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface ClientService {

    Optional<ViewClientsByAdminDTO> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<ViewClientsByAdminDTO> getAllClientsAsDto(Pageable pageable);











    List<Client> getAllClients();
    void deleteClientById(Long id);
    void deleteAllOrdersOfClientById(Long id);

    void register(ClientRegisterDTO clientRegisterDTO);

}