package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

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

    // Constructors

    public Appliance() {
    }

    public Appliance(Long id, String name, Category category, String model,
                     Manufacturer manufacturer, PowerType powerType,
                     String characteristic, String description,
                     Integer power, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.model = model;
        this.manufacturer = manufacturer;
        this.powerType = powerType;
        this.characteristic = characteristic;
        this.description = description;
        this.power = power;
        this.price = price;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public PowerType getPowerType() {
        return powerType;
    }

    public void setPowerType(PowerType powerType) {
        this.powerType = powerType;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appliance)) return false;
        Appliance appliance = (Appliance) o;
        return Objects.equals(id, appliance.id)
                && Objects.equals(name, appliance.name)
                && category == appliance.category
                && Objects.equals(model, appliance.model)
                && Objects.equals(manufacturer, appliance.manufacturer)
                && powerType == appliance.powerType
                && Objects.equals(characteristic, appliance.characteristic)
                && Objects.equals(description, appliance.description)
                && Objects.equals(power, appliance.power)
                && Objects.equals(price, appliance.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, model, manufacturer, powerType, characteristic, description, power, price);
    }

    // toString()

    @Override
    public String toString() {
        return "Appliance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", model='" + model + '\'' +
                ", manufacturer=" + manufacturer +
                ", powerType=" + powerType +
                ", characteristic='" + characteristic + '\'' +
                ", description='" + description + '\'' +
                ", power=" + power +
                ", price=" + price +
                '}';
    }
}