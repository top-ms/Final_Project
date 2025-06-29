//package com.epam.rd.autocode.assessment.appliances.controllers__;
//
//import com.epam.rd.autocode.assessment.appliances.model.Employee;
//import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/employees")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//public class EmployeeController {
//
//    private final EmployeeService employeeService;
//
//    public EmployeeController(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }
//
//    @GetMapping
//    public String viewHomePage(Model model) {
//        model.addAttribute("employees", employeeService.getAllEmployee());
//        return "employee/employees";
//    }
//
//    @GetMapping("/add")
//    public String showNewEmployeeForm(Model model) {
//        model.addAttribute("employee", new Employee());
//        return "employee/newEmployee";
//    }
//
//    @PostMapping("/add-employee")
//    public String addNewClient(@ModelAttribute("employee") Employee employee) {
//        employeeService.register(employee);
//        return "redirect:/employees";
//    }
//
//    @GetMapping("/{id}/delete")
//    public String deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteEmployeeById(id);
//        return "redirect:/employees";
//    }
//
//}
