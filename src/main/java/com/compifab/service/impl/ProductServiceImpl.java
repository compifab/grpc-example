package com.compifab.service.impl;

import com.compifab.dto.ProductInputDTO;
import com.compifab.dto.ProductOutputDTO;
import com.compifab.exception.ProductAlreadyExistsException;
import com.compifab.exception.ProductNotFoundException;
import com.compifab.mapper.ProductMapper;
import com.compifab.repository.ProductRepository;
import com.compifab.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO productInputDTO) {
        existsByName(productInputDTO.getName());

        var product = ProductMapper.productInputDTOToProduct(productInputDTO);
        var createdProduct = productRepository.save(product);
        return ProductMapper.productToProductOutputDTO(createdProduct);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductMapper.productToProductOutputDTO(product);
    }

    @Override
    public void delete(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        var products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::productToProductOutputDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void existsByName(String name) {
        productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new ProductAlreadyExistsException(name);
                });
    }
}
