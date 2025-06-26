package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;

@Controller
public class AuthController {

    private final ClientService clientService;

    // 🔗 Інжектимо сервіс для роботи з клієнтами
    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    // 📄 Показати форму реєстрації
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client()); // додати порожній об’єкт для заповнення форми
        System.out.println("showRegistrationForm() method called. Client object added to the model. Client object: " + model.getAttribute("client") + "");
        return "entrance/register"; // Thymeleaf-шаблон register.html
    }

    // 📝 Обробити реєстрацію користувача
    @PostMapping("/register")
    public String registerClient(@ModelAttribute("client") Client client) {
        // 🧠 Генеруємо бонусну карту (можеш винести це в сервіс — не критично)
        String bonusCard = generateRandomCardNumber();
        client.setCard(bonusCard);

        // 🔐 Викликаємо сервіс, де пароль хешується й юзер зберігається в базу
        clientService.register(client);
        System.out.println("Client registered: " + client.getEmail() + " with card: " + client.getPassword() + "");
        // ✅ Після реєстрації перекидуємо на логін
        return "redirect:/login";
    }

    // 🔐 Показати форму логіну (Spring Security сам обробить POST /login)
    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("showLoginForm() method called. Client object added to the model. Client object: " + "null" + "");
        return "entrance/login";
    }

    // 🏠 Показати головну сторінку після входу
    @GetMapping("/main")
    public String showMainPage() {
        System.out.println("showMainPage() method called. Client object added to the model. Client object: " + "null" + "");
        return "index"; // можеш створити index.html для тесту
    }

    // 🔢 Метод генерації картки (можна залишити тут або винести в сервіс)
    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder card = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            card.append(random.nextInt(10));
        }
        System.out.println("Generated card number: " + card.toString() + "");
        return card.toString();
    }
}