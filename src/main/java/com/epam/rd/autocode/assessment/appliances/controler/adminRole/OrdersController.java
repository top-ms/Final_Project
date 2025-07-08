package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ViewClientsByAdminDTO;
import com.epam.rd.autocode.assessment.appliances.dto.employeeDTO.ViewEmployeesDTO;
import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.CreateOrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class OrdersController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;

    public OrdersController(OrderService orderService, ClientService clientService,
                            EmployeeService employeeService, ApplianceService applianceService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.applianceService = applianceService;
    }

    @GetMapping("orders/add")
    public String showNewOrderForm(Model model) {
        model.addAttribute("createOrderDTO", new CreateOrderDTO());
        return "admin/order/newOrder";
    }

    @PostMapping("orders/search-client")
    public String searchClient(@ModelAttribute CreateOrderDTO createOrderDTO,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        String email = createOrderDTO.getClientEmail();
        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, введіть email клієнта");
            return "redirect:/admin/orders/add";
        }
        Optional<ViewClientsByAdminDTO> clientOptional = clientService.findByEmail(email.trim());
        if (clientOptional.isPresent()) {
            model.addAttribute("createOrderDTO", createOrderDTO);
            model.addAttribute("foundClient", clientOptional.get());
            model.addAttribute("clientFound", true);
        } else {
            model.addAttribute("createOrderDTO", createOrderDTO);
            model.addAttribute("error", "Клієнта з таким email не знайдено. Спробуйте ще раз.");
            model.addAttribute("clientFound", false);
        }
        return "admin/order/newOrder";
    }

    @PostMapping("orders/create")
    public String createOrder(@ModelAttribute CreateOrderDTO createOrderDTO,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        String clientEmail = createOrderDTO.getClientEmail();
        String currentUserEmail = authentication.getName();
        Optional<ViewClientsByAdminDTO> clientOptional = clientService.findByEmail(clientEmail);
        if (!clientOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Клієнта не знайдено");
            return "redirect:/admin/orders/add";
        }
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        Employee employee = null;
        if (isEmployee) {
            employee = employeeService.findEmployeeEntityByEmail(currentUserEmail);
        }
        if (isAdmin) {
            List<Employee> employees = employeeService.getAllEmployees();
            if (!employees.isEmpty()) {
                employee = employees.get(0);
            } else {
                redirectAttributes.addFlashAttribute("error", "У системі немає жодного працівника");
                return "redirect:/admin/orders/add";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Недостатньо прав для створення замовлення");
            return "redirect:/admin/orders/add";
        }
        if (employee == null) {
            redirectAttributes.addFlashAttribute("error", "Не вдалося визначити працівника для замовлення");
            return "redirect:/admin/orders/add";
        }
        Orders order = new Orders();
        Client client = clientService.findClientEntityByEmail(clientEmail);
        order.setClient(client);
        order.setEmployee(employee);
        order.setApproved(false);
        orderService.saveNewOrder(order);
        redirectAttributes.addFlashAttribute("success", "Замовлення успішно створено");
        return "redirect:/admin/orders";
    }

    @GetMapping("orders")
    public String listOfOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "asc") String priceSort,
                               @RequestParam(required = false) Long employeeId,
                               @RequestParam(required = false) Boolean approved,
                               Model model) {
        Sort.Direction sortDirection = priceSort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        Page<ViewOrdersDTO> ordersPage;
        if (employeeId != null && approved != null) {
            ordersPage = orderService.getOrdersByEmployeeIdAndApproved(employeeId, approved, pageable);
        } else if (employeeId != null) {
            ordersPage = orderService.getOrdersByEmployeeId(employeeId, pageable);
        } else if (approved != null) {
            ordersPage = orderService.getOrdersByApproved(approved, pageable);
        } else {
            ordersPage = orderService.getAllOrdersAsDto(pageable);
        }
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("priceSort", priceSort);
        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedApproved", approved);
        return "admin/order/orders";
    }

    @GetMapping("orders/search")
    public String searchOrders(@RequestParam("orderId") Long orderId, Model model) {
        if (orderId == null || orderId == 0) {
            return "redirect:/admin/orders";
        }
        Optional<ViewOrdersDTO> orderOptional = orderService.findOrderDtoById(orderId);
        if (orderOptional.isPresent()) {
            model.addAttribute("ordersPage", new PageImpl<>(List.of(orderOptional.get())));
        } else {
            model.addAttribute("notFound", true);
            model.addAttribute("ordersPage", Page.empty());
        }
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "admin/order/orders";
    }

    @GetMapping("orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page) {
        orderService.deleteOrderById(id);
        return "redirect:/admin/orders?page=" + page;
    }

    @GetMapping("orders/{id}/edit")
    public String editOrder(@PathVariable("id") Long id, Model model) {
        Orders orders = orderService.findById(id);
        Set<OrderRow> rows = orders.getOrderRowSet();
        model.addAttribute("order", orders);
        model.addAttribute("rows", rows);
        return "admin/order/editOrder";
    }

    @GetMapping("orders/{id}/choice-appliance")
    public String choiceAppliance(@PathVariable("id") Long id, Model model) {
        List<Appliance> appliances = applianceService.getAllAppliances();
        model.addAttribute("appliances", appliances);
        model.addAttribute("ordersId", id);
        return "admin/order/choiceAppliance";
    }

    @PostMapping("orders/{id}/add-into-order")
    public String addIntoOrder(@PathVariable("id") Long orderId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long number,
                               RedirectAttributes redirectAttributes) {
        orderService.saveNewOrderRowById(orderId, applianceId, number);
        redirectAttributes.addFlashAttribute("success", "Товар успішно додано до замовлення");
        return "redirect:/admin/orders/" + orderId + "/edit";
    }

    @GetMapping("orders/{id}/approved")
    public String approveOrder(@PathVariable Long id,
                               @RequestParam(defaultValue = "0") int page) {
        orderService.setApproved(id, true);
        return "redirect:/admin/orders?page=" + page;
    }

    @GetMapping("orders/{id}/unapproved")
    public String unapproveOrder(@PathVariable Long id,
                                 @RequestParam(defaultValue = "0") int page) {
        orderService.setApproved(id, false);
        return "redirect:/admin/orders?page=" + page;
    }

    @GetMapping("orders/{orderId}/edit/row/{rowId}/delete")
    public String deleteOrderRow(@PathVariable Long orderId,
                                 @PathVariable Long rowId) {
        orderService.deleteOrderRowById(rowId);
        return "redirect:/admin/orders/" + orderId + "/edit";
    }
}