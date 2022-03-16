package com.compifab.resources;

import br.com.compifab.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void setup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldCreateProduct() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Product Test")
                .setPrice(20.34)
                .setQuantityInStock(10)
                .build();

        var productResponse = serviceBlockingStub.create(productRequest);

        assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantityInStock")
                .isEqualTo(productResponse);
    }

    @Test
    public void shouldThrowExceptionWhenDuplicatedCreateProduct() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Product A")
                .setPrice(20.34)
                .setQuantityInStock(10)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Product Product A already registered");
    }

    @Test
    public void shouldReturnProductById() {
        var productRequest = RequestById.newBuilder().setId(1L).build();
        var productResponse = serviceBlockingStub.findById(productRequest);

        assertThat(productResponse.getId())
                .isEqualTo(productRequest.getId());
    }

    @Test
    public void shouldThrowExceptionWhenIdNotExists() {
        var productRequest = RequestById.newBuilder().setId(100L).build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.findById(productRequest))
                .withMessage("NOT_FOUND: Product 100 not found");
    }

    @Test
    @DisplayName("Should delete product when product id is valid")
    public void shouldDeleteProductById() {
        var productRequest = RequestById.newBuilder().setId(1L).build();
        assertThatNoException().isThrownBy(() -> serviceBlockingStub.delete(productRequest));
    }

    @Test
    @DisplayName("Should throw exception when delete product id is not valid")
    public void shouldThrowExceptionWhenDeleteProductIdIsNotValid() {
        var productRequest = RequestById.newBuilder().setId(100L).build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.delete(productRequest))
                .withMessage("NOT_FOUND: Product 100 not found");
    }

    @Test
    @DisplayName("Should return all products")
    public void shouldReturnAllProducts() {
        var emptyRequest = EmptyRequest.newBuilder().build();
        var productResponseList = serviceBlockingStub.findAll(emptyRequest);

        assertThat(productResponseList).isInstanceOf(ProductResponseList.class);
        assertThat(productResponseList.getProductsCount()).isEqualTo(2);
        assertThat(productResponseList.getProductsList())
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                        tuple(1L, "Product A", 10.99, 10),
                        tuple(2L, "Product B", 10.99, 10)
                );
    }
}
