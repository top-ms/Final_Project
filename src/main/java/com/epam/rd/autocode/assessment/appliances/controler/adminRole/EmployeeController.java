package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.UserEditDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.EmployeeRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
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
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ClientService clientService;

    public EmployeeController(EmployeeService employeeService, ClientService clientService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @GetMapping("employees")
    public String viewListOfClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            Model model) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));
        Page<ViewEmployeesDTO> employeesPage = employeeService.getAllEmployeesAsDto(pageable);
        model.addAttribute("employeesPage", employeesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "admin/employee/employees";
    }

    @GetMapping("employees/search")
    public String searchEmployees(@RequestParam("email") String email, Model model) {
        if (email == null || email == "") {
            return "redirect:/admin/employees";
        }
        Optional<ViewEmployeesDTO> employeeOptional = employeeService.findByEmail(email);
        if (employeeOptional.isPresent()) {
            model.addAttribute("employeesPage", new PageImpl<>(List.of(employeeOptional.get())));
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("employeesPage", Page.empty());
        }
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "admin/employee/employees";
    }

    @GetMapping("employees/add")
    public String showFormForAddingEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/employee/newEmployee";
    }

    @PostMapping("employees/add-employee")
    public String addNewEmployee(@Valid @ModelAttribute("employee") EmployeeRegisterDTO dto, BindingResult bindingResult) {
        if (employeeService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.client.email");
            return "admin/employee/newEmployee";
        }
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors:");
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println("Field: " + error.getField());
                System.out.println("Rejected value: " + error.getRejectedValue());
                System.out.println("Message: " + error.getDefaultMessage());
            });
            return "admin/employee/newEmployee";
        }
        employeeService.register(dto);
        return "redirect:/admin/employees";
    }

    @GetMapping("employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id,
                                 @RequestParam(defaultValue = "0") int page) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/admin/employees?page=" + page;
    }

    @GetMapping("employees/{id}/edit")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Optional<UserEditDTO> employeeOptional = employeeService.findByIdForEdit(id);
        if (employeeOptional.isEmpty()) {
            return "redirect:/admin/employees";
        }
        model.addAttribute("employee", employeeOptional.get());
        model.addAttribute("isEdit", true);
        return "admin/employee/editEmployee";
    }

    @PostMapping("employees/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") UserEditDTO userEditDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        userEditDTO.setId(id);
        Optional<Employee> existingEmployee = employeeService.findById(id);
        if (existingEmployee.isEmpty()) {
            return "redirect:/admin/employees";
        }
        if (!existingEmployee.get().getEmail().equals(userEditDTO.getEmail())) {
            if (employeeService.existsByEmail(userEditDTO.getEmail())) {
                bindingResult.rejectValue("email", "error.employee.email", "Працівник з таким email вже існує");
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin/employee/editEmployee";
        }
        employeeService.updateEmployee(userEditDTO);
        return "redirect:/admin/employees";
    }
}
