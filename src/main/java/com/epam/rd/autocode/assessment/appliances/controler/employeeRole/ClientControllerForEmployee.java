package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public class ClientControllerForEmployee {

    private final ClientService clientService;

    public ClientControllerForEmployee(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("clients")
    public String viewListOfClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            Model model) {

        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));
        Page<ViewClientsByAdminDTO> clientsPage = clientService.getAllClientsAsDto(pageable);

        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("clients", clientService.getAllClients()); // якщо використовуєш ще десь
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientsPage.getTotalPages());
        model.addAttribute("sort", sort); // щоб пагінація пам’ятала sort

        return "employee/client/clients";
    }

    @GetMapping("clients/search")
    public String searchClients(@RequestParam("email") String email, Model model) {
        if (email == null || email == "") {
            return "redirect:/employee/clients";
        }
        Optional<ViewClientsByAdminDTO> clientOptional = clientService.findByEmail(email);

        if (clientOptional.isPresent()) {
            model.addAttribute("clientsPage", new PageImpl<>(List.of(clientOptional.get())));
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("clientsPage", Page.empty());
        }

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);


        return "employee/client/clients";
    }

    @GetMapping("clients/add")
    public String showFormForAddingClient(Model model) {
        model.addAttribute("client", new Client());
        return "employee/client/newClient";
    }


    @PostMapping("/register")
    public String registerClient(@Valid @ModelAttribute("client") ClientRegisterDTO dto,
                                 BindingResult bindingResult,
                                 Model model) {


        if (clientService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.client.email");
            return "entrance/register";
        }
        if (bindingResult.hasErrors()) {
            return "entrance/register";
        }
        clientService.register(dto);
        return "redirect:/login";
    }

    @PostMapping("clients/add-client")
    public String addNewClient(@Valid @ModelAttribute("client") ClientRegisterDTO dto, BindingResult bindingResult) {

        if (clientService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.client.email");
            return "employee/client/newClient";
        }
        if (bindingResult.hasErrors()) {
            return "employee/client/newClient";
        }
        clientService.register(dto);
        return "redirect:/employee/clients";
    }

    @GetMapping("clients/{id}/delete")
    public String deleteClient(@PathVariable Long id,
                               @RequestParam(defaultValue = "0") int page) {
        clientService.deleteClientById(id);
        return "redirect:/employee/clients?page=" + page;
    }

















    // Додай ці методи до ClientControllerForEmployee:

    @GetMapping("clients/{id}/edit")
    public String showEditClientForm(@PathVariable Long id, Model model) {
        Optional<UserEditDTO> clientOptional = clientService.findByIdForEdit(id);

        if (clientOptional.isEmpty()) {
            return "redirect:/admin/clients";
        }

        model.addAttribute("client", clientOptional.get());
        model.addAttribute("isEdit", true);
        return "employee/client/editClient";
    }

    @PostMapping("clients/{id}/update")
    public String updateClient(@PathVariable Long id,
                               @Valid @ModelAttribute("client") UserEditDTO userEditDTO,
                               BindingResult bindingResult,
                               Model model) {

        // Встановлюємо ID з URL
        userEditDTO.setId(id);

        // Перевіряємо чи існує клієнт з таким email (окрім поточного)
        Optional<Client> existingClient = clientService.findById(id);
        if (existingClient.isEmpty()) {
            return "redirect:/employee/clients";
        }

        // Перевіряємо унікальність email (якщо змінився)
        if (!existingClient.get().getEmail().equals(userEditDTO.getEmail())) {
            if (clientService.existsByEmail(userEditDTO.getEmail())) {
                bindingResult.rejectValue("email", "error.client.email", "Клієнт з таким email вже існує");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "employee/client/editClient";
        }

        clientService.updateClient(userEditDTO);
        return "redirect:/employee/clients";
    }
}
