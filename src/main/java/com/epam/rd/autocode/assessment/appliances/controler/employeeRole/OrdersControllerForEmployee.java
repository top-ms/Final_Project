package com.epam.rd.autocode.assessment.appliances.controler.employeeRole;

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
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
public class OrdersControllerForEmployee {

    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;

    public OrdersControllerForEmployee(OrderService orderService, ClientService clientService, EmployeeService employeeService, ApplianceService applianceService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.applianceService = applianceService;
    }

    @GetMapping("orders")
    public String listOfOrders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "employee/order/orders";
    }

    @GetMapping("orders/add")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Orders()); // Порожній Order для форми

        model.addAttribute("clients", clientService.getAllClients()); // для селекта клієнтів
        model.addAttribute("employees", employeeService.getAllEmployees()); // для селекта працівників

        return "employee/order/newOrder"; // Назва шаблону (html), де твоя форма
    }

    @PostMapping("orders/add-order")
    public String addOrder(@ModelAttribute Orders orders) {
        orders.setApproved(false);
        orderService.saveNewOrder(orders);
        return "redirect:/employee/orders";
    }

    @GetMapping("orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/employee/orders";
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
        return "employee/order/editOrder";
    }


    @GetMapping("orders/{id}/choice-appliance")
    public String choiceAppliance(@PathVariable("id") Long id, Model model) {
        List<Appliance> appliances = applianceService.getAllAppliances(); // витягуємо всі товари
        model.addAttribute("appliances", appliances);
        model.addAttribute("ordersId", id); // передаємо ID замовлення, щоб знати куди додавати

        return "employee/order/choiceAppliance";
    }

    @PostMapping("orders/{id}/add-into-order")
    public String addIntoOrder(@PathVariable("id") Long orderId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long number) {

        orderService.saveNewOrderRowById(orderId, applianceId, number);
        System.out.println("addIntoOrder: " + orderId + " " + applianceId + " " + number);
        return "redirect:/employee/orders/" + orderId + "/edit";
    }

    @GetMapping("orders/{id}/approved")
    public String approveOrder(@PathVariable Long id) {
        orderService.setApproved(id, true);
        return "redirect:/employee/orders";
    }

    @GetMapping("orders/{id}/unapproved")
    public String unapproveOrder(@PathVariable Long id) {
        orderService.setApproved(id, false);
        return "redirect:/employee/orders";
    }

    @GetMapping("orders/{orderId}/edit/row/{rowId}/delete")
    public String deleteOrderRow(
            @PathVariable Long orderId,
            @PathVariable Long rowId
    ) {
        orderService.deleteOrderRowById(rowId);
        return "redirect:/employee/orders/" + orderId + "/edit";
    }
}
