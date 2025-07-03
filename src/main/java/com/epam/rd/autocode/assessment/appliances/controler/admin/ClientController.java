package com.epam.rd.autocode.assessment.appliances.controler.admin;

import com.epam.rd.autocode.assessment.appliances.dto.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ViewClientsByAdminDTO;
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
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
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

        return "admin/client/clients";
    }





    @GetMapping("clients/search")
    public String searchClients(@RequestParam("email") String email, Model model) {
        Optional<ViewClientsByAdminDTO> clientOptional = clientService.findByEmail(email);

        if (clientOptional.isPresent()) {
            // Обгортаємо одного клієнта у список і передаємо як Page
            model.addAttribute("clientsPage", new PageImpl<>(List.of(clientOptional.get())));
        } else {
            // Додаємо ознаку, що нічого не знайдено
            model.addAttribute("notFound", true);
            model.addAttribute("clientsPage", Page.empty()); // 🛡️ додаємо порожню сторінку

        }

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);


        return "admin/client/clients";
    }



























    @GetMapping("clients/add")
    public String showFormForAddingClient(Model model) {
        model.addAttribute("client", new Client());
        return "admin/client/newClient";
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
            return "admin/client/newClient";
        }
        if (bindingResult.hasErrors()) {
            return "admin/client/newClient";
        }
        clientService.register(dto);
        return "redirect:/admin/clients";
    }

    @GetMapping("clients/{id}/delete")
    public String deleteClient(@PathVariable Long id,
                               @RequestParam(defaultValue = "0") int page) {
        clientService.deleteClientById(id);
        return "redirect:/admin/clients?page=" + page;
    }

}
