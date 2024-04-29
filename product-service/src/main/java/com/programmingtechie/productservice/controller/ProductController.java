package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// RESTful 웹 서비스의 컨트롤러
@RequestMapping("/api/product")
// /api/product 경로로 들어오는 요청을 받아주는 클래스를 지정한다.
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    // POST HTTP 요청을 처리하는 메서드를 지정해줌
    @ResponseStatus(HttpStatus.CREATED)
    // 해당 메서드의 HTTP 상태를 지정한것으로, CREATED는 HTTP 201이며, 이것을 반환하도록 하라는 의미다.
    public void createProduct(@RequestBody ProductRequest productRequest){
        // 제품 생성요청(post) 를 받으면 controller에서 하는게 아닌, service 게층에서 비즈니스 로직을 처리해야함
        // productRequest를 받아서 실행한다.
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    // HTTP 상태를 지정한 것으로, OK는 HTTP 200으로 반환하라는 의미다. 여기서 200은 클라이언트 요청이 성공했다는 의미다.
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProduct();
    }
}
