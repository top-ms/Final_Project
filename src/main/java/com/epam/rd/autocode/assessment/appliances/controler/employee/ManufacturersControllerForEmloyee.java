package com.epam.rd.autocode.assessment.appliances.controler.employee;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public class ManufacturersControllerForEmloyee {

    private final ManufacturerService manufacturerService;

    public ManufacturersControllerForEmloyee(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manufacturers")
    public String viewListOfManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        return "employee/manufacture/manufacturers";
    }

    @GetMapping("/manufacturers/add")
    public String showFormForAddingNewManufacturer(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "employee/manufacture/newManufacturer";
    }

    @PostMapping("/manufacturers/add-manufacturer")
    public String addNewManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.saveNewManufacturer(manufacturer);
        return "redirect:/employee/manufacturers";
    }

    @GetMapping("manufacturers/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:/employee/manufacturers";
    }
}
