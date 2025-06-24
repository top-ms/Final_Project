package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;
import com.epam.rd.autocode.assessment.appliances.service.impl.ClientServiceImpl;
import com.epam.rd.autocode.assessment.appliances.service.impl.EmployeeServiceImpl;
import com.epam.rd.autocode.assessment.appliances.service.impl.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final ClientServiceImpl clientServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;
    private final ApplianceServiceImpl applianceServiceImpl;

    public OrdersController(OrderServiceImpl orderServiceImpl, ClientServiceImpl clientServiceImpl, EmployeeServiceImpl employeeServiceImpl, ApplianceServiceImpl applianceServiceImpl) {
        this.orderService = orderServiceImpl;
        this.clientServiceImpl = clientServiceImpl;
        this.employeeServiceImpl = employeeServiceImpl;
        this.applianceServiceImpl = applianceServiceImpl;
    }

    @GetMapping
    public String listOfOrders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/orders";
    }

    @GetMapping("/add")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Orders()); // Порожній Order для форми

        model.addAttribute("clients", clientServiceImpl.getAllClients()); // для селекта клієнтів
        model.addAttribute("employees", employeeServiceImpl.getAllEmployee()); // для селекта працівників

        return "order/newOrder"; // Назва шаблону (html), де твоя форма
    }

    @PostMapping("/add-order")
    public String addOrder(@ModelAttribute Orders orders) {
        orders.setApproved(false);
        orderService.saveNewOrder(orders);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String editOrder(@PathVariable("id") Long id, Model model) {
        // 1. Знайти замовлення
        Orders orders = orderService.findById(id);

        // 2. Отримати всі рядки (OrderRow) замовлення
        Set<OrderRow> rows = orders.getOrderRowSet();

        // 3. Передати до моделі
        model.addAttribute("order", orders);
        model.addAttribute("rows", rows);

        // 4. Повернути HTML сторінку редагування
        return "order/editOrder";
    }


    @GetMapping("/{id}/choice-appliance")
    public String choiceAppliance(@PathVariable("id") Long id, Model model) {
        List<Appliance> appliances = applianceServiceImpl.getAllAppliances(); // витягуємо всі товари
        model.addAttribute("appliances", appliances);
        model.addAttribute("ordersId", id); // передаємо ID замовлення, щоб знати куди додавати

        return "order/choiceAppliance";
    }

    @PostMapping("/{id}/add-into-order")
    public String addIntoOrder(@PathVariable("id") Long orderId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long number) {

        orderService.saveNewOrderRowById(orderId, applianceId, number);
        System.out.println("addIntoOrder: " + orderId + " " + applianceId + " " + number);
        return "redirect:/orders/" + orderId + "/edit";
    }

    @GetMapping("/{id}/approved")
    public String approveOrder(@PathVariable Long id) {
        orderService.setApproved(id, true);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/unapproved")
    public String unapproveOrder(@PathVariable Long id) {
        orderService.setApproved(id, false);
        return "redirect:/orders";
    }

    @GetMapping("/{orderId}/edit/row/{rowId}/delete")
    public String deleteOrderRow(
            @PathVariable Long orderId,
            @PathVariable Long rowId
    ) {
        orderService.deleteOrderRowById(rowId);
        return "redirect:/orders/" + orderId + "/edit";
    }
}
