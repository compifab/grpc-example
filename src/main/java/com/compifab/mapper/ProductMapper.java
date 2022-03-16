package com.compifab.mapper;

import com.compifab.domain.Product;
import com.compifab.dto.ProductInputDTO;
import com.compifab.dto.ProductOutputDTO;

public class ProductMapper {

    public static ProductOutputDTO productToProductOutputDTO(Product product) {
        return new ProductOutputDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantityInStock());
    }

    public static Product productInputDTOToProduct(ProductInputDTO productInputDTO) {
        return new Product(null, productInputDTO.getName(), productInputDTO.getPrice(), productInputDTO.getQuantityInStock());
    }
}
