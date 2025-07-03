package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
@RequestMapping("/employee")
public class ClientControllerForEmployee {

    private final ClientService clientService;

    public ClientControllerForEmployee(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("clients")
    public String viewListOfClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "employee/client/clients";
    }

    @GetMapping("clients/add")
    public String showFormForAddingClient(Model model) {
        model.addAttribute("client", new Client());
        return "employee/client/newClient";
    }

    @PostMapping("clients/add-client")
    public String addNewClient(@Valid @ModelAttribute("client") ClientRegisterDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee/client/newClient";
        }
        clientService.register(dto);
        return "redirect:/employee/clients";
    }

    @GetMapping("clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return "redirect:/employee/clients";
    }
}
