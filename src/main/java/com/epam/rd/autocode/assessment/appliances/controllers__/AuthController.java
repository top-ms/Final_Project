//package com.epam.rd.autocode.assessment.appliances.controllers__;
//
//import com.epam.rd.autocode.assessment.appliances.model.Client;
//import com.epam.rd.autocode.assessment.appliances.service.ClientService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class EntranceAuthController {
//
//    private final ClientService clientService;
//
//    // 🔗 Інжектимо сервіс для роботи з клієнтами
//    public EntranceAuthController(ClientService clientService) {
//        this.clientService = clientService;
//    }
//
//    // 📄 Показати форму реєстрації
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("client", new Client()); // додати порожній об’єкт для заповнення форми
//        System.out.println("showRegistrationForm() method called. Client object added to the model. Client object: " + model.getAttribute("client") + "");
//        return "entrance/register"; // Thymeleaf-шаблон register.html
//    }
//
//    // 📝 Обробити реєстрацію користувача
//    @PostMapping("/register")
//    public String registerClient(@ModelAttribute("client") Client client) {
//        // 🔐 Викликаємо сервіс, де пароль хешується й юзер зберігається в базу
//        clientService.register(client);
//        // ✅ Після реєстрації перекидуємо на логін
//        return "redirect:/login";
//    }
//
//    // 🔐 Показати форму логіну (Spring Security сам обробить POST /login)
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "entrance/login";
//    }
//
//    // 🏠 Показати головну сторінку після входу
//    @GetMapping("/main")
//    public String showMainPage() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("User: " + auth.getName());
//        System.out.println("Authorities: " + auth.getAuthorities());
//        System.out.println("showMainPage() method called. Client object added to the model. Client object: " + "null" + "");
//        return "index"; // можеш створити index.html для тесту
//    }
//
//}