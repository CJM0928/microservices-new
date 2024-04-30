package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString()); // 임의의 ID를 주문번호 String으로 제공한다

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call Inventory Service, and Place order if product is in stock
        // 클라이언트에서 데이터를 읽기위해서는 아래의 본문이 필요함. ( Mono )
        // 동기식 요청이 가능하면 8082에 대한 동기식 요청을 해야함.
        // result 가 true라면, 재고가 있다는 의미로 주문해야함.

        // 웹 클라이언트는 이 형식으로 uri를 구성하며,
        // uri을 build 할 수있도록 uriBuilder를 사용한다.
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                    .uri("http://localhost:8082/api/inventory" ,
                            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

        // inventoryResponsesArray 배열 안에서 재고가 있는지 확인
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

        // postman 에서 이러한 값을 입력하게되면 throw를 해줘야한다. -> InventoryServiceApplication에 입력된 값에의해.
//        {
//            "orderLineItemsDtoList":[
//            {
//                "skuCode":"iphone_13",
//                    "price":1200,
//                    "quantity":1
//            },
//            {
//                "skuCode":"iphone_13_red",
//                    "price":1200,
//                    "quantity":1
//            }
//              ]
//        }

        // 만약 iphone_13_red 를 지운다면, 재고가 있으니 throw를 발생하지 않는다.
        // 이것이 의미하는바는, order를 할때 inventory가 어떤상황에 처있는지 api 통신을 할수있다는 의미다.
        // 게다가 이것은 여러개의 api 통신이 아닌, 하나의 api 통신으로 수행할수있다는 것이다.
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
