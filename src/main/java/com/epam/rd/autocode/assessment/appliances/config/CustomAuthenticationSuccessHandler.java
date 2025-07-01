//package com.epam.rd.autocode.assessment.appliances.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication)
//            throws IOException, ServletException {
//
//        for (GrantedAuthority auth : authentication.getAuthorities()) {
//            String role = auth.getAuthority();
//
//            if (role.equals("ROLE_ADMIN")) {
//                response.sendRedirect("/admin/orders");
//                return;
//            } else if (role.equals("ROLE_EMPLOYEE")) {
//                response.sendRedirect("/employee/orders");
//                return;
//            } else if (role.equals("ROLE_CLIENT")) {
//                response.sendRedirect("/client/orders");
//                return;
//            }
//        }
//
//        // Якщо роль не знайдена — редірект на загальну сторінку або помилку
//        response.sendRedirect("/access-denied");
//    }
//}