package com.epam.rd.autocode.assessment.appliances.dto;

import java.math.BigDecimal;

public class ApplianceDTO {
    private Long id;
    private String name;
    private String model;
    private String category;       // ENUM: BIG, SMALL
    private Long manufacturerId;   // зв’язок з Manufacturer
    private String powerType;      // ENUM: AC220, AC110, ACCUMULATOR
    private String characteristic;
    private String description;
    private Integer power;
    private BigDecimal price;
}