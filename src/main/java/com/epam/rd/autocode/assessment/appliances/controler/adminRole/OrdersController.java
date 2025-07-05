package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("orders")
    public String listOfOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "asc") String priceSort,
                               @RequestParam(required = false) Long employeeId,
                               @RequestParam(required = false) Boolean approved,
                               Model model) {

        // Визначаємо напрямок сортування за ціною
        Sort.Direction sortDirection = priceSort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));

        Page<ViewOrdersDTO> ordersPage;

        // Фільтрація
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

    @GetMapping("orders/add")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Orders());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "admin/order/newOrder";
    }

    @PostMapping("orders/add-order")
    public String addOrder(@ModelAttribute Orders orders) {
        orders.setApproved(false);
        orderService.saveNewOrder(orders);
        return "redirect:/admin/orders";
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
                               @RequestParam("numbers") Long number) {
        orderService.saveNewOrderRowById(orderId, applianceId, number);
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