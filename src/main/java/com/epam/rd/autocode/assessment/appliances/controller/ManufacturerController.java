package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.impl.ManufacturerServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerServiceImpl manufacturerServiceImpl;

    public ManufacturerController(ManufacturerServiceImpl manufacturerServiceImpl) {
        this.manufacturerServiceImpl = manufacturerServiceImpl;
    }

    @GetMapping
    public String listManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerServiceImpl.getAllManufacturers());
        return "manufacture/manufacturers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacture/newManufacturer";
    }

    @PostMapping("/add-manufacturer")
    public String add(@ModelAttribute Manufacturer manufacturer) {
        manufacturerServiceImpl.saveNewManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }
}
