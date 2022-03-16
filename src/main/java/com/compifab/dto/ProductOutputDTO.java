package com.compifab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOutputDTO {
    private final Long id;
    private final String name;
    private final Double price;
    private final Integer quantityInStock;
}
