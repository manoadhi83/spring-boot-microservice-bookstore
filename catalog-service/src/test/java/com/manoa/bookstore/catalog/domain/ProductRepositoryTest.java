package com.manoa.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:tc:mysql:8.0:///bookstore"
        }
)
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> productList = repository.findAll();
        assertThat(productList).hasSize(15);
    }


}