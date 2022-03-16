package com.compifab.resources;

import br.com.compifab.*;
import com.compifab.dto.ProductInputDTO;
import com.compifab.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private final IProductService productService;

    public ProductResource(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        var productInput = new ProductInputDTO(request.getName(), request.getPrice(), request.getQuantityInStock());

        var productOutput = productService.create(productInput);

        var productResponse = ProductResponse.newBuilder()
                .setId(productOutput.getId())
                .setName(productOutput.getName())
                .setPrice(productOutput.getPrice())
                .setQuantityInStock(productOutput.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        var productOutput = productService.findById(request.getId());

        var productResponse = ProductResponse.newBuilder()
                .setId(productOutput.getId())
                .setName(productOutput.getName())
                .setPrice(productOutput.getPrice())
                .setQuantityInStock(productOutput.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
        productService.delete(request.getId());
        responseObserver.onNext(EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        var productsOutput = productService.findAll();

        var productResponses = productsOutput.stream()
                .map(productOutput -> {
                    return ProductResponse.newBuilder()
                            .setId(productOutput.getId())
                            .setName(productOutput.getName())
                            .setPrice(productOutput.getPrice())
                            .setQuantityInStock(productOutput.getQuantityInStock())
                            .build();
                })
                .collect(Collectors.toList());

        ProductResponseList response = ProductResponseList.newBuilder()
                .addAllProducts(productResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
