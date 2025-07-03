package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ManufacturersController {

    private final ManufacturerService manufacturerService;

    public ManufacturersController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manufacturers")
    public String viewListOfManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        return "admin/manufacture/manufacturers";
    }

    @GetMapping("/manufacturers/add")
    public String showFormForAddingNewManufacturer(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "admin/manufacture/newManufacturer";
    }

    @PostMapping("/manufacturers/add-manufacturer")
    public String addNewManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.saveNewManufacturer(manufacturer);
        return "redirect:/admin/manufacturers";
    }

    @GetMapping("manufacturers/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:/admin/manufacturers";
    }
}
