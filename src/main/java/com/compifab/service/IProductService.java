package com.compifab.service;

import com.compifab.dto.ProductInputDTO;
import com.compifab.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {

    ProductOutputDTO create(ProductInputDTO productInputDTO);

    ProductOutputDTO findById(Long id);

    void delete(Long id);

    List<ProductOutputDTO> findAll();

    void existsByName(String name);
}
