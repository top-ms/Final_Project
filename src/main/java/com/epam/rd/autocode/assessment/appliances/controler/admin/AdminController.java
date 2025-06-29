package com.epam.rd.autocode.assessment.appliances.controler.admin;

import com.epam.rd.autocode.assessment.appliances.dto.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.service.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService, ClientService clientService, EmployeeService employeeService, ManufacturerService manufacturerService, ApplianceService applianceService, OrderService orderService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public String viewListOfAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin/admin/admins";
    }

    @GetMapping("/admins/add")
    public String showFormForAddingAdmin(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/admin/newAdmin";
    }

    @PostMapping("/admins/add-admin")
    public String addNewAdmin(Admin admin) {
        adminService.register(admin);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admins/{id}/delete")
    public String deleteAdminById(@PathVariable Long id) {
        adminService.deleteAdminById(id);
        return "redirect:/admin/admins";
    }
}
