package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.RegistrationAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.adminDTO.ViewAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public String viewListOfAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            Model model) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));
        Page<ViewAdminDTO> adminsPage = adminService.getAllAdminsAsDto(pageable);
        model.addAttribute("adminsPage", adminsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", adminsPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "admin/admin/admins";
    }


    @GetMapping("admins/search")
    public String searchAdmins(@RequestParam("email") String email, Model model) {
        if (email == null || email == "") {
            return "redirect:/admin/admins";
        }
        Optional<ViewAdminDTO> adminOptional = adminService.getAdminByEmail(email);
        if (adminOptional.isPresent()) {
            model.addAttribute("adminsPage", adminOptional.get());
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("adminsPage", Page.empty());
        }
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "admin/admin/admins";
    }

    @GetMapping("admins/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page) {
        adminService.deleteAdminById(id);
        return "redirect:/admin/admins?page=" + page;
    }

    @GetMapping("/admins/add")
    public String showFormForAddingAdmin(Model model) {
        model.addAttribute("admin", new RegistrationAdminDTO());
        return "admin/admin/newAdmin";
    }

    @PostMapping("/admins/add-admin")
    public String addNewAdmin(@Valid @ModelAttribute("admin") RegistrationAdminDTO dto,
                              BindingResult bindingResult,
                              Model model) {
        if (adminService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.client.email");
            return "admin/admin/newAdmin";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", dto);
            return "admin/admin/newAdmin";
        }
        adminService.register(dto);
        return "redirect:/admin/admins";
    }

    @GetMapping("admins/{id}/edit")
    public String showEditAdminForm(@PathVariable Long id, Model model) {
        Optional<UserEditDTO> adminOptional = adminService.findByIdForEdit(id);
        if (adminOptional.isEmpty()) {
            return "redirect:/admin/admins";
        }
        model.addAttribute("admin", adminOptional.get());
        model.addAttribute("isEdit", true);
        return "admin/admin/editAdmin";
    }

    @PostMapping("admins/{id}/update")
    public String updateAdmin(@PathVariable Long id,
                              @Valid @ModelAttribute("admin") UserEditDTO userEditDTO,
                              BindingResult bindingResult,
                              Model model) {
        userEditDTO.setId(id);
        Optional<Admin> existingAdmin = adminService.findById(id);
        if (existingAdmin.isEmpty()) {
            return "redirect:/admin/admins";
        }
        if (!existingAdmin.get().getEmail().equals(userEditDTO.getEmail())) {
            if (adminService.existsByEmail(userEditDTO.getEmail())) {
                bindingResult.rejectValue("email", "error.admin.email", "Адміністратор з таким email вже існує");
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin/admin/editAdmin";
        }
        adminService.updateAdmin(userEditDTO);
        return "redirect:/admin/admins";
    }

}
