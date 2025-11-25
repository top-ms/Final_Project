package com.epam.rd.autocode.assessment.appliances.controler.clientRole;

import com.epam.rd.autocode.assessment.appliances.dto.orderDTO.ViewOrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Random;

@Controller
@RequestMapping("/client")
@PreAuthorize("hasAuthority('ROLE_CLIENT')")
public class OrdersControllerForClient {

    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;

    public OrdersControllerForClient(OrderService orderService, ClientService clientService,
                                     EmployeeService employeeService, ApplianceService applianceService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.applianceService = applianceService;
    }

    private Client getCurrentClient(Authentication authentication) {
        String currentClientEmail = authentication.getName();
        return clientService.findClientEntityByEmail(currentClientEmail);
    }

    private boolean isOrderOwner(Long orderId, Authentication authentication) {
        Orders order = orderService.findById(orderId);
        Client currentClient = getCurrentClient(authentication);
        return order.getClient().getId().equals(currentClient.getId());
    }

    @PostMapping("orders/create")
    public String createOrder(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            Client currentClient = getCurrentClient(authentication);
            if (currentClient == null) {
                redirectAttributes.addFlashAttribute("error", "Клієнта не знайдено");
                return "redirect:/client/orders";
            }
            List<Employee> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "У системі немає жодного працівника");
                return "redirect:/client/orders";
            }
            Random random = new Random();
            Employee randomEmployee = employees.get(random.nextInt(employees.size()));
            Orders order = new Orders();
            order.setClient(currentClient);
            order.setEmployee(randomEmployee);
            order.setApproved(false);
            orderService.saveNewOrder(order);
            redirectAttributes.addFlashAttribute("success", "Замовлення успішно створено");
            return "redirect:/client/orders/" + order.getId() + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при створенні замовлення: " + e.getMessage());
            return "redirect:/client/orders";
        }
    }

    @GetMapping("orders")
    public String listOfOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "asc") String priceSort,
                               @RequestParam(required = false) Long employeeId,
                               @RequestParam(required = false) Boolean approved,
                               Authentication authentication,
                               Model model) {
        Client currentClient = getCurrentClient(authentication);
        if (currentClient == null) {
            model.addAttribute("error", "Клієнта не знайдено");
            return "client/order/orders";
        }
        Sort.Direction sortDirection = priceSort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        Page<ViewOrdersDTO> ordersPage;
        Long clientId = currentClient.getId();
        if (employeeId != null && approved != null) {
            ordersPage = orderService.getOrdersByClientIdAndEmployeeIdAndApproved(clientId, employeeId, approved, pageable);
        } else if (employeeId != null) {
            ordersPage = orderService.getOrdersByClientIdAndEmployeeId(clientId, employeeId, pageable);
        } else if (approved != null) {
            ordersPage = orderService.getOrdersByClientIdAndApproved(clientId, approved, pageable);
        } else {
            ordersPage = orderService.getOrdersByClientId(clientId, pageable);
        }
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("priceSort", priceSort);
        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedApproved", approved);
        return "client/order/orders";
    }

    @GetMapping("orders/search")
    public String searchOrders(@RequestParam("orderId") Long orderId,
                               Authentication authentication,
                               Model model) {
        if (orderId == null || orderId == 0) {
            return "redirect:/client/orders";
        }
        if (!isOrderOwner(orderId, authentication)) {
            model.addAttribute("error", "Замовлення не знайдено або ви не маєте доступу до нього");
            model.addAttribute("ordersPage", Page.empty());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "client/order/orders";
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
        return "client/order/orders";
    }

    @GetMapping("orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(id, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права видаляти це замовлення");
            return "redirect:/client/orders?page=" + page;
        }
        orderService.deleteOrderById(id);
        redirectAttributes.addFlashAttribute("success", "Замовлення успішно видалено");
        return "redirect:/client/orders?page=" + page;
    }

    @GetMapping("orders/{id}/edit")
    public String editOrder(@PathVariable("id") Long id,
                            Authentication authentication,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(id, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права редагувати це замовлення");
            return "redirect:/client/orders";
        }
        Orders orders = orderService.findById(id);
        Set<OrderRow> rows = orders.getOrderRowSet();
        model.addAttribute("order", orders);
        model.addAttribute("rows", rows);
        return "client/order/editOrder";
    }

    @GetMapping("orders/{id}/choice-appliance")
    public String choiceAppliance(@PathVariable("id") Long id,
                                  Authentication authentication,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(id, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права редагувати це замовлення");
            return "redirect:/client/orders";
        }
        List<Appliance> appliances = applianceService.getAllAppliances();
        model.addAttribute("appliances", appliances);
        model.addAttribute("ordersId", id);
        return "client/order/choiceAppliance";
    }

    @PostMapping("orders/{id}/add-into-order")
    public String addIntoOrder(@PathVariable("id") Long orderId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long number,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(orderId, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права редагувати це замовлення");
            return "redirect:/client/orders";
        }
        orderService.saveNewOrderRowById(orderId, applianceId, number);
        redirectAttributes.addFlashAttribute("success", "Товар успішно додано до замовлення");
        return "redirect:/client/orders/" + orderId + "/edit";
    }

    @GetMapping("orders/{id}/approved")
    public String approveOrder(@PathVariable Long id,
                               @RequestParam(defaultValue = "0") int page,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(id, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права змінювати статус цього замовлення");
            return "redirect:/client/orders?page=" + page;
        }
        orderService.setApproved(id, true);
        redirectAttributes.addFlashAttribute("success", "Замовлення затверджено");
        return "redirect:/client/orders?page=" + page;
    }

    @GetMapping("orders/{id}/unapproved")
    public String unapproveOrder(@PathVariable Long id,
                                 @RequestParam(defaultValue = "0") int page,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(id, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права змінювати статус цього замовлення");
            return "redirect:/client/orders?page=" + page;
        }
        orderService.setApproved(id, false);
        redirectAttributes.addFlashAttribute("success", "Затвердження замовлення скасовано");
        return "redirect:/client/orders?page=" + page;
    }

    @GetMapping("orders/{orderId}/edit/row/{rowId}/delete")
    public String deleteOrderRow(@PathVariable Long orderId,
                                 @PathVariable Long rowId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        if (!isOrderOwner(orderId, authentication)) {
            redirectAttributes.addFlashAttribute("error", "Ви не маєте права редагувати це замовлення");
            return "redirect:/client/orders";
        }
        orderService.deleteOrderRowById(rowId);
        redirectAttributes.addFlashAttribute("success", "Товар успішно видалено з замовлення");
        return "redirect:/client/orders/" + orderId + "/edit";
    }
}