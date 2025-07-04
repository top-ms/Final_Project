package com.epam.rd.autocode.assessment.appliances.dto.adminDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewAdminDTO {
    private Long id;
    private String name;
    private String email;
}
