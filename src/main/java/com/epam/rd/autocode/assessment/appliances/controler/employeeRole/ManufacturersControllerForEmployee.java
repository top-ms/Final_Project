package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.AddNewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.dto.manufacturerDTO.ViewManufacturerAsDTO;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
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
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public class ManufacturersControllerForEmployee {

    private final ManufacturerService manufacturerService;

    public ManufacturersControllerForEmployee(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manufacturers")
    public String viewListOfManufacturers(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "asc") String sort,
                                          Model model) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));
        Page<ViewManufacturerAsDTO> manufacturersPage = manufacturerService.getAllManufacturersAsDto(pageable);
        model.addAttribute("manufacturersPage", manufacturersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", manufacturersPage.getTotalPages());
        model.addAttribute("sort", sort);
        return "employee/manufacture/manufacturers";
    }

    @GetMapping("manufacturers/search")
    public String searchManufacturers(@RequestParam("name") String name, Model model) {
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/employee/manufacturers";
        }
        Optional<ViewManufacturerAsDTO> manufacturerOptional = manufacturerService.getByName(name);
        if (manufacturerOptional.isPresent()) {
            List<ViewManufacturerAsDTO> list = List.of(manufacturerOptional.get());
            Page<ViewManufacturerAsDTO> page = new PageImpl<>(list);
            model.addAttribute("manufacturersPage", page);
            model.addAttribute("notFound", false);
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("manufacturersPage", Page.empty());
        }
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("sort", "asc");
        return "employee/manufacture/manufacturers";
    }

    @GetMapping("/manufacturers/add")
    public String showFormForAddingNewManufacturer(Model model) {
        model.addAttribute("manufacturer", new AddNewManufacturerAsDTO());
        return "employee/manufacture/newManufacturer";
    }

    @PostMapping("/manufacturers/add-manufacturer")
    public String addNewManufacturer(@Valid @ModelAttribute("manufacturer") AddNewManufacturerAsDTO dto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (manufacturerService.existsByName(dto.getName())) {
            bindingResult.rejectValue("name", "error.manufacturer.name");
            return "employee/manufacture/newManufacturer";
        }
        if (bindingResult.hasErrors()) {
            return "employee/manufacture/newManufacturer";
        }
        manufacturerService.saveNewManufacturer(dto);
        return "redirect:/employee/manufacturers";
    }

    @GetMapping("manufacturers/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page) {
        manufacturerService.deleteManufacturerById(id);
        return "redirect:/employee/manufacturers?page=" + page;
    }
}
