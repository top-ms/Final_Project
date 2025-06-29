//package com.epam.rd.autocode.assessment.appliances.controllers__;
//
//import com.epam.rd.autocode.assessment.appliances.model.Client;
//import com.epam.rd.autocode.assessment.appliances.service.ClientService;
//import com.epam.rd.autocode.assessment.appliances.service.impl.ClientServiceImpl;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/clients")
//public class ClientController {
//
//    private final ClientService clientService;
//
//    public ClientController(ClientServiceImpl clientService, ClientService clientService1) {
//        this.clientService = clientService1;
//    }
//
//
//    @GetMapping
//    public String viewHomePage(Model model) {
//        model.addAttribute("clients", clientService.getAllClients());
//        return "client/clients";
//    }
//
//    @GetMapping("/add")
//    public String showNewClientForm(Model model) {
//        model.addAttribute("client", new Client());
//        return "client/newClient";
//    }
//
//    @PostMapping("/add-client")
//    public String addNewClient(@ModelAttribute("client") Client client) {
//        clientService.register(client);
//        return "redirect:/clients";
//    }
//
//    @GetMapping("/{id}/delete")
//    public String deleteClient(@PathVariable Long id) {
//        clientService.deleteClientById(id);
//        return "redirect:/clients";
//    }
//
//
//}
