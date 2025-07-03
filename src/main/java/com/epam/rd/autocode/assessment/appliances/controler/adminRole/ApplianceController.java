package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ApplianceController {

    private final ApplianceService applianceService;

    public ApplianceController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @GetMapping("/appliances")
    public String viewListOfAppliance(Model model) {
        model.addAttribute("appliance", applianceService.getAllAppliances());
        return "admin/appliance/appliances";
    }


    @GetMapping("/appliances/add")
    public String showNewApplianceForm(Model model) {
        model.addAttribute("appliance", new Appliance());
        model.addAttribute("categories", Category.values());
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", applianceService.getAllManufacturers());
        return "admin/appliance/newAppliance";
    }

    @PostMapping("/appliances/add-appliance")
    public String addNewAppliance(@ModelAttribute("appliance") Appliance appliance) {
        applianceService.addNewAppliance(appliance);
        return "redirect:/admin/appliances";
    }
}
