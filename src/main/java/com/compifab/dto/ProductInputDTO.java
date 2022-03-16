package com.compifab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductInputDTO {
    private final String name;
    private final Double price;
    private final Integer quantityInStock;
}
