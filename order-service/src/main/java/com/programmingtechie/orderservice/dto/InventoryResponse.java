package com.programmingtechie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    // skuCode에 대한 정보와, 재고가 있는지 검사여부를 저장할예정(isInStock)
    private String skuCode;
    private boolean isInStock;
}
