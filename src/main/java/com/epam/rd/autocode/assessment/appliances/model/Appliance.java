package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PowerType powerType;

    @Column(nullable = false)
    private String characteristic;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer power;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

}
