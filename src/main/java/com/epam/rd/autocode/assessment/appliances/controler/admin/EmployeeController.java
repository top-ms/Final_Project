package com.epam.rd.autocode.assessment.appliances.controler.admin;


import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String viewListOfEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        return "admin/employee/employees";
    }

    @GetMapping("employees/add")
    public String showFormForAddingEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/employee/newEmployee";
    }

    @PostMapping("employees/add-employee")
    public String addNewEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.register(employee);
        return "redirect:/admin/employees";
    }

    @GetMapping("employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/admin/employees";
    }
}
