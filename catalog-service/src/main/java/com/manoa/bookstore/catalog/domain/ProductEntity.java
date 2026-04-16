package com.manoa.bookstore.catalog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Product code is required") private String code;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Product name is required") private String name;

    private String description;

    @Column(name = "image_url")
    private String imageURL;

    @NotNull(message = "Product price is required") @DecimalMin("0.1") @Column(nullable = false)
    private BigDecimal price;

    public ProductEntity() {}

    public ProductEntity(String code, String name, String imageURL, BigDecimal price, String description) {
        this.code = code;
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
