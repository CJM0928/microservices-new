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
    private final WebClient.Builder webClientBuilder;

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
        // localhost:8082 같은 하드코딩된 port 를 쓰지않도록,
        InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        // inventoryResponsesArray 배열 안에서 재고가 있는지 확인
        // 재고가 없으면 throw 수행
        boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
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

        // 아래의 경우는 정상적으로 Order Placed Successfully 해준다.
//        {
//            "orderLineItemsDtoList":[
//            {
//                "skuCode":"iphone_13",
//                    "price":1200,
//                    "quantity":1
//            }
//            ]
//        }

//         만약 iphone_13_red 를 지운다면, 재고가 있으니 throw를 발생하지 않는다.
//         이것이 의미하는바는, order를 할때 inventory가 어떤상황에 처있는지 api 통신을 할수있다는 의미다.
//         게다가 이것은 여러개의 api 통신이 아닌, 하나의 api 통신으로 수행할수있다는 것이다.
//
//         다중 인스턴스로 inventory 서비스를 하고나서, postman을 통해 order-service로 제품을 주문하면
//         java.net.UnknownHostException: Failed to resolve 'inventory-service' [A(1)] after 4 queries
//         오류가 발생하며, 이것은 4번 쿼리 이후 inventory-service를 해결하지 못한것이다.
//         이 이유는 http://localhost:8761/ 을 보면, 여러 버전의 인벤토리 서비스가 있기때문이다.
//
//         여러 인스턴스가 존재한다면, discovery server가 모든 인스턴스를 등록하고, 주문서비스가 인벤토리 서비스를 요청할때마다
//         인벤토리 서비스의 대한 모든정보가 있기에, 어떤 인벤토리의 인스턴스를 호출해야할지 혼동이 온다.
//         이 인스턴스들 에서 해줘야 할 일은 하나하나씩 살펴보는것으로, 우리가 가지고있는 기능을 활성화 할 수 있도록
//         인스턴스를 하나씩 호출해야한다는것이다. 유레카 클라이언트에서 "클라이언트 측 로드 밸런싱(Client Side LoadBalancing)" 을 활성화 하려면
//         클라이언트 핀을 구성할 때 애너테이션을 추가해야한다. (orderservice의 WebClientConfig)
//
//         PostMan 이용시, 애플리케이션을 작동후 30초 정도 기다리고나서 post를 해줘야 에러가 안나온다.

//        여기서, DiscoveryServer를 종료 하더라도, postman에 POST 요청하면 정상적으로 처리된다.
//        이유는 우리는 DiscoveryServer에서 찾고있는동안, Client가 로컬 복사본내 레지스트리의 로컬 복사본을 저장해뒀기 때문이다
//        단, 여기서 InventoryService를 종료하고 다시 실행한다음, POST 요청을하면 500 error가 발생하는데, 이 이유는
//        OrderService를 열려고 한다. 다만, 로컬복사본에서 재고 서비스를 확인하려고해도 어느곳에도 발견하지 못했기에 재고를 가져오려 했으나,
//        이때, DiscoveryServer가 종료되었기에 가져올수 없다. 따라서 500 error가 발생한다.
//        이때, 다시 DiscoveryServer를 실행한다면, 우선 Eureka에 Instance가 안보이는걸 알 수 있고, 이 이유는
//        서비스가 HeartBeat를 보내는데 30초정도 기다려야한다.
//        기다리고나면 다시 인스턴스들(재고서비스,주문서비스,제품서비스)이 백업된것을 볼 수있고, 이 상태에서 postman에 POST요청을 한다면,
//
//        즉 DiscoveryServer(검색서버)를 삭제(종료) 했더라도, 기존 레지스트리의 로컬 복사본을 이용해서 InventoryService를 호출가능하며,
//        만약 여기에서 InventoryService까지 삭제(종료)한다면, 로컬에있는 복사본이 사라지므로 postman에서 OrderService를 하더라도 500error가 발생함.
//        
//        여기까지 Discovery 서버와 Client 간의 관계를 정리함.
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
