package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class Test {

    private final AdminService adminService;
    public Test(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/add")
    public void test() {
        adminService.register(new Admin());
    }
}
