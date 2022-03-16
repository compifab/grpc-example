package com.compifab.mapper;

import com.compifab.domain.Product;
import com.compifab.dto.ProductInputDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    @Test
    public void shouldMapperFromProductToProductOutputDTO() {
        var product = new Product(1L, "Product Name", 10.00, 10);
        var productOutputDTO = ProductMapper.productToProductOutputDTO(product);

        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productOutputDTO);
    }

    @Test
    public void shouldMapperFromProductInputDTOToProduct() {
        var productInput = new ProductInputDTO("Product Name", 10.00, 10);
        var product = ProductMapper.productInputDTOToProduct(productInput);

        assertThat(productInput)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
}
