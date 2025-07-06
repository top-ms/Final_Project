package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ViewApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public class ApplianceControllerForEmployee {

    private final ApplianceService applianceService;

    public ApplianceControllerForEmployee(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @GetMapping("/appliances")
    public String viewListOfAppliances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            Model model) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));


        Page<ViewApplianceDTO> appliancesPage = applianceService.getAllManufacturersAsDto(pageable);


        model.addAttribute("appliancesPage", appliancesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appliancesPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "employee/appliance/appliances";
    }

    @GetMapping("appliances/search")
    public String searchAppliances(@RequestParam("name") String name, Model model) {
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/employee/appliances";
        }

        Optional<ViewApplianceDTO> applianceOptional = applianceService.getByName(name);

        if (applianceOptional.isPresent()) {
            // Обгортаємо один об'єкт у Page
            Page<ViewApplianceDTO> page = new PageImpl<>(List.of(applianceOptional.get()));
            model.addAttribute("appliancesPage", page);
            model.addAttribute("notFound", false);
            model.addAttribute("totalPages", 1);
        } else {
            model.addAttribute("appliancesPage", Page.empty());
            model.addAttribute("notFound", true);
            model.addAttribute("totalPages", 1);
        }

        model.addAttribute("currentPage", 0);

        return "employee/appliance/appliances";
    }


    @GetMapping("appliances/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page) {
        applianceService.deleteApplianceById(id);
        return "redirect:/employee/appliances?page=" + page;
    }

    @GetMapping("/appliances/add")
    public String showNewApplianceForm(Model model) {
        model.addAttribute("appliance", new Appliance());
        model.addAttribute("categories", Category.values());
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", applianceService.getAllManufacturers());
        return "employee/appliance/newAppliance";
    }

    @PostMapping("/appliances/add-appliance")
    public String addNewAppliance(@ModelAttribute("appliance") ViewApplianceDTO dto) {
        applianceService.addNewAppliance(dto);
        return "redirect:/employee/appliances";
    }



    @GetMapping("/appliances/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<ViewApplianceDTO> appliance = applianceService.getApplianceById(id);
        System.out.println("Appliance " + appliance.get());

        model.addAttribute("categories", Category.values());
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", applianceService.getAllManufacturers());
        model.addAttribute("appliance", appliance.get());

        return "employee/appliance/editAppliance";
    }

    @PostMapping("/appliances/{id}/update-appliance")
    public String updateAppliance(@PathVariable("id") Long id,
                                  @ModelAttribute("appliance") ViewApplianceDTO dto) {
        applianceService.updateAppliance(id, dto);
        return "redirect:/employee/appliances";
    }





















}
