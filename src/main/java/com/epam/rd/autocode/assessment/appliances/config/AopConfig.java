package com.epam.rd.autocode.assessment.appliances.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    // Ця конфігурація вмикає AOP у вашому додатку
    // @EnableAspectJAutoProxy говорить Spring сканувати @Aspect класи
}