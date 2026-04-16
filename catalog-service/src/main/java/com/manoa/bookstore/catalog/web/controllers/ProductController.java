package com.manoa.bookstore.catalog.web.controllers;

import com.manoa.bookstore.catalog.domain.PagedResult;
import com.manoa.bookstore.catalog.domain.Product;
import com.manoa.bookstore.catalog.domain.ProductNotFoundException;
import com.manoa.bookstore.catalog.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        LOGGER.info("Fetching products for page: {}", pageNumber);
        return service.getAllProducts(pageNumber);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        LOGGER.info("Fetching product for code: {}", code);
        return service.getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
