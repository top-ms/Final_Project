package com.epam.rd.autocode.assessment.appliances.controler.admin;

import com.epam.rd.autocode.assessment.appliances.dto.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("clients")
    public String viewListOfClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "admin/client/clients";
    }

    @GetMapping("clients/add")
    public String showFormForAddingClient(Model model) {
        model.addAttribute("client", new Client());
        return "admin/client/newClient";
    }

    @PostMapping("clients/add-client")
    public String addNewClient(@Valid @ModelAttribute("client") ClientRegisterDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/newClient";
        }
        clientService.register(dto);
        return "redirect:/admin/clients";
    }

    @GetMapping("clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return "redirect:/admin/clients";
    }
}
