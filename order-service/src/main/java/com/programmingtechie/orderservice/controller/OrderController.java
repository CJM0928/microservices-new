package com.programmingtechie.orderservice.controller;

import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// 클라이언트에서 컨트롤러로 주문요청을 받고
// 컨트롤러는 주문요청을 주문서비로 전달한다.
// 주문서비스내에서 주문 요청을 개체에 매핑하고, 이 주문을 주문저장소에 저장한다.
// 그리고 다시 여기로 돌아와서 다시 주문 컨트롤러에서 주문 서비스를 호출해야한다.

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        // null check 오류떄문에 만들어봄
        if(orderRequest != null && orderRequest.getOrderLineItemsDtoList() != null) {
            orderService.placeOrder(orderRequest);
            return "Order Placed Successfully";
        } else{
            return "Order Request is null";
        }
    }
}
