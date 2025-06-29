//package com.epam.rd.autocode.assessment.appliances.controllers__;
//
//import com.epam.rd.autocode.assessment.appliances.model.Admin;
//import com.epam.rd.autocode.assessment.appliances.service.AdminService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/test")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping
//    public String getAddAdminPage() {
//        return "test/test";
//    }
//    @GetMapping("/add")
//    public void test() {
//        adminService.register(new Admin());
//    }
//}
