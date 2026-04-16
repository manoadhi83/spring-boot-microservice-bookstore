package com.manoa.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(properties = {"spring.test.database.replace=none", "spring.datasource.url=jdbc:tc:mysql:8.0:///bookstore"})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> productList = repository.findAll();
        assertThat(productList).hasSize(15);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity productEntity = repository.findByCode("P100").orElseThrow();
        assertThat(productEntity.getCode()).isEqualTo("P100");
        assertThat(productEntity.getName()).isEqualTo("The Hunger Games");
        assertThat(productEntity.getPrice()).isEqualTo("34.00");
        assertThat(productEntity.getDescription())
                .isEqualTo("Winning will make you famous. Losing means certain death...");
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotFound() {
        assertThat(repository.findByCode("100")).isEmpty();
    }
}
