package com.epam.rd.autocode.assessment.appliances.dto.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewOrdersDTO {
    private Long id;
    private String clientName;
    private String clientEmail;
    private String employeeName;
    private String employeeEmail;
    private BigDecimal price;
    private Boolean approved;
}