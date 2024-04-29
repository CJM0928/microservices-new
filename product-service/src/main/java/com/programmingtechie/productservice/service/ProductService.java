package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.respository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// 이 클래스가 서비스 계층임을 애너테이션으로 지정
@Service
//Lombok 에서 제공하는 애너테이션으로, 생성자를 대신 만들어준다.
@RequiredArgsConstructor
// Lombok 에서 제공하는 애너테이션으로, 로그정보를 만들어준다
// log.info("product " + product.getId() +" is saved"); 를 사용하지않아도 된다.
@Slf4j
public class ProductService {

    // 생성자를 만들어줘야하는데, 애너테이션으로 RequiredArgsConstructor을 사용하면 된다.
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        // product 생성하는데 빌드패턴으로 만든다.
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // save 메서드를 이용하면, product를 성공적으로 저장해준다.
        productRepository.save(product);
        // Slf4j 애너테이션에 의해서 info 방식을 변경할수있다.
        log.info("product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProduct(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
