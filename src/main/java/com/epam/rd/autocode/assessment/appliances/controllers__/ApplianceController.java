//package com.epam.rd.autocode.assessment.appliances.controllers__;
//
//import com.epam.rd.autocode.assessment.appliances.model.Appliance;
//import com.epam.rd.autocode.assessment.appliances.model.Category;
//import com.epam.rd.autocode.assessment.appliances.model.PowerType;
//import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/appliances")
//public class ApplianceController {
//
//    private final ApplianceService applianceService;
//
//    public ApplianceController(ApplianceService applianceService) {
//        this.applianceService = applianceService;
//    }
//
//    @GetMapping
//    public String viewHomePage(Model model) {
//        model.addAttribute("appliance", applianceService.getAllAppliance());
//        return "appliance/appliances";
//    }
//
//
//    @GetMapping("/add")
//    public String showNewApplianceForm(Model model) {
//        model.addAttribute("appliance", new Appliance());
//        model.addAttribute("categories", Category.values());
//        model.addAttribute("powerTypes", PowerType.values());
//        model.addAttribute("manufacturers", applianceService.getAllManufacturers());
//        return "appliance/newAppliance";
//    }
//
//    @PostMapping("/add-appliance")
//    public String addNewAppliance(@ModelAttribute("appliance") Appliance appliance) {
//        applianceService.addNewAppliance(appliance);
//        return "redirect:/appliances";
//    }
//}
//
//
//
//
