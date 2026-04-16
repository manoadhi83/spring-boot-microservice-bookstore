package com.manoa.bookstore.catalog.domain;

import com.manoa.bookstore.catalog.ApplicationProperties;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    private final ApplicationProperties properties;

    ProductService(ProductRepository repository, ApplicationProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    public PagedResult<Product> getAllProducts(int pageNumber) {
        Sort sort = Sort.by("name").ascending();
        pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumber, properties.pageSize(), sort);
        Page<Product> products = repository.findAll(pageable).map(ProductMapper::toProduct);
        return new PagedResult<>(
                products.getContent(),
                products.getTotalElements(),
                products.getNumber() + 1,
                products.getTotalPages(),
                products.isFirst(),
                products.isLast(),
                products.hasNext(),
                products.hasPrevious());
    }

    public Optional<Product> getProductByCode(String code) {
        return repository.findByCode(code).map(ProductMapper::toProduct);
    }
}
