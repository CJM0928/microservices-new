package com.programmingtechie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Data 애너테이션은 getter setter 모두 갖는다.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    // orderLineItemsDtoList 이 배열이름이, skuCode , price 등등 post 할때 쓰는 친구다.
    // {
    //    "orderLineItemsDtoList":[
    //        {
    //            "skuCode":"iphone_13",
    //            "price":1200,
    //            "quantity": 1
    //        }
    //    ]
    //}
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
