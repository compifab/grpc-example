package com.compifab.service.impl;

import com.compifab.domain.Product;
import com.compifab.dto.ProductInputDTO;
import com.compifab.exception.ProductAlreadyExistsException;
import com.compifab.exception.ProductNotFoundException;
import com.compifab.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should crate product when input data is valid")
    public void shouldCreateProduct() {
        var product = new Product(1L, "Product Name", 10.00, 10);

        when(productRepository.save(any())).thenReturn(product);

        var productInput = new ProductInputDTO("Product Name", 10.00, 10);

        var productOutput = productService.create(productInput);

        assertThat(productOutput)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("Should throw exception when try to insert duplicate data")
    public void shouldThrowExceptionWhenDuplicateData() {
        var product = new Product(1L, "Product Name", 10.00, 10);

        when(productRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(product));

        var productInput = new ProductInputDTO("Product Name", 10.00, 10);

        assertThatExceptionOfType(ProductAlreadyExistsException.class)
                .isThrownBy(() -> productService.create(productInput));
    }

    @Test
    @DisplayName("Should return product by id when id is valid")
    public void shouldReturnProductById() {
        Long id = 1L;
        var product = new Product(1L, "Product Name", 10.00, 10);

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        var productOutput = productService.findById(id);

        assertThat(productOutput)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("Should throw exception when id does not exist")
    public void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 1L;
        var product = new Product(1L, "Product Name", 10.00, 10);

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.findById(id));
    }

    @Test
    @DisplayName("Should delete product")
    public void shouldDeleteProduct() {
        Long id = 1L;
        var product = new Product(1L, "Product Name", 10.00, 10);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        assertThatNoException().isThrownBy(() -> productService.delete(id));
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    public void shouldThrowExceptionWhenProductNotFound() {
        Long id = 1L;
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.delete(id));
    }

    @Test
    @DisplayName("Should return all products")
    public void shouldReturnAllProducts() {
        var products = List.of(
                new Product(1L, "Product A", 10.00, 10),
                new Product(2L, "Product B", 20.00, 15)
        );

        when(productRepository.findAll()).thenReturn(products);

        var productsOutput = productService.findAll();

        assertThat(productsOutput)
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                        tuple(1L, "Product A", 10.00, 10),
                        tuple(2L, "Product B", 20.00, 15)
                );
    }
}
