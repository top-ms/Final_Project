package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.applianceDTO.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ApplianceController {

    private final ApplianceService applianceService;

    public ApplianceController(ApplianceService applianceService) {
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


        Page<ApplianceDTO> appliancesPage = applianceService.getAllManufacturersAsDto(pageable);


        model.addAttribute("appliancesPage", appliancesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appliancesPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "admin/appliance/appliances";
    }

    @GetMapping("appliances/search")
    public String searchAppliances(@RequestParam("name") String name, Model model) {
        if (name == null || name == "") {
            return "redirect:/admin/appliances";
        }

        Optional<ApplianceDTO> applianceOptional = applianceService.getByName(name);
        if (applianceOptional.isPresent()) {
            model.addAttribute("appliancesPage", applianceOptional.get());
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("appliancesPage", Page.empty());
        }
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "admin/appliance/appliances";
    }


    @GetMapping("appliances/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page) {
        applianceService.deleteApplianceById(id);
        return "redirect:/admin/appliances?page=" + page;
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
    public String addNewAppliance(@ModelAttribute("appliance") ApplianceDTO dto) {
        System.out.println("Before");
        applianceService.addNewAppliance(dto);
        System.out.println("After");
        return "redirect:/admin/appliances";
    }

}
