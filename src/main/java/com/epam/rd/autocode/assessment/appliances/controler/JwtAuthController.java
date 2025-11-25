package com.epam.rd.autocode.assessment.appliances.controler;

import com.epam.rd.autocode.assessment.appliances.dto.clientDTO.ClientRegisterDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Controller
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthController.class);

    public JwtAuthController(AuthenticationManager authenticationManager, JwtService jwtService, ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.clientService = clientService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "entrance/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("username") String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);
            System.out.println("JWT Token: " + jwtToken);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(7 * 24 * 60 * 10);
            response.addCookie(jwtCookie);
            for (String headerName : response.getHeaderNames()) {
                System.out.println(headerName + ": " + response.getHeader(headerName));
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/admin/orders";
                } else if (role.equals("ROLE_EMPLOYEE")) {
                    return "redirect:/employee/orders";
                } else if (role.equals("ROLE_CLIENT")) {
                    return "redirect:/client/orders";
                }
            }
            return "redirect:/access-denied";
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", email, e);
            return "redirect:/login?error=invalid_credentials";
        } catch (Exception e) {
            logger.error("Login error for user: {}", email, e);
            return "redirect:/login?error=system_error";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client());
        System.out.println("showRegistrationForm() method called. Client object added to the model. Client object: " + model.getAttribute("client") + "");
        return "entrance/register";
    }

    @PostMapping("/register")
    public String registerClient(@Valid @ModelAttribute("client") ClientRegisterDTO dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (clientService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.client.email");
            return "entrance/register";
        }
        if (bindingResult.hasErrors()) {
            return "entrance/register";
        }
        clientService.register(dto);
        return "redirect:/login";
    }
}