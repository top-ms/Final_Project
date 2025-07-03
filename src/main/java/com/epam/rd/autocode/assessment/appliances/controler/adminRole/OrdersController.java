package com.epam.rd.autocode.assessment.appliances.controler.adminRole;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class OrdersController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;

    public OrdersController(OrderService orderService, ClientService clientService, EmployeeService employeeService, ApplianceService applianceService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.applianceService = applianceService;
    }

    @GetMapping("orders")
    public String listOfOrders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order/orders";
    }

    @GetMapping("orders/add")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Orders()); // Порожній Order для форми

        model.addAttribute("clients", clientService.getAllClients()); // для селекта клієнтів
        model.addAttribute("employees", employeeService.getAllEmployees()); // для селекта працівників

        return "admin/order/newOrder"; // Назва шаблону (html), де твоя форма
    }

    @PostMapping("orders/add-order")
    public String addOrder(@ModelAttribute Orders orders) {
        orders.setApproved(false);
        orderService.saveNewOrder(orders);
        return "redirect:/admin/orders";
    }

    @GetMapping("orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("orders/{id}/edit")
    public String editOrder(@PathVariable("id") Long id, Model model) {
        // 1. Знайти замовлення
        Orders orders = orderService.findById(id);

        // 2. Отримати всі рядки (OrderRow) замовлення
        Set<OrderRow> rows = orders.getOrderRowSet();

        // 3. Передати до моделі
        model.addAttribute("order", orders);
        model.addAttribute("rows", rows);

        // 4. Повернути HTML сторінку редагування
        return "admin/order/editOrder";
    }


    @GetMapping("orders/{id}/choice-appliance")
    public String choiceAppliance(@PathVariable("id") Long id, Model model) {
        List<Appliance> appliances = applianceService.getAllAppliances(); // витягуємо всі товари
        model.addAttribute("appliances", appliances);
        model.addAttribute("ordersId", id); // передаємо ID замовлення, щоб знати куди додавати

        return "admin/order/choiceAppliance";
    }

    @PostMapping("orders/{id}/add-into-order")
    public String addIntoOrder(@PathVariable("id") Long orderId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long number) {

        orderService.saveNewOrderRowById(orderId, applianceId, number);
        System.out.println("addIntoOrder: " + orderId + " " + applianceId + " " + number);
        return "redirect:/admin/orders/" + orderId + "/edit";
    }

    @GetMapping("orders/{id}/approved")
    public String approveOrder(@PathVariable Long id) {
        orderService.setApproved(id, true);
        return "redirect:/admin/orders";
    }

    @GetMapping("orders/{id}/unapproved")
    public String unapproveOrder(@PathVariable Long id) {
        orderService.setApproved(id, false);
        return "redirect:/admin/orders";
    }

    @GetMapping("orders/{orderId}/edit/row/{rowId}/delete")
    public String deleteOrderRow(
            @PathVariable Long orderId,
            @PathVariable Long rowId
    ) {
        orderService.deleteOrderRowById(rowId);
        return "redirect:/admin/orders/" + orderId + "/edit";
    }
}
