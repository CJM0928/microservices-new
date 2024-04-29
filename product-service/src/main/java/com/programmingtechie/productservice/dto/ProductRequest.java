package com.programmingtechie.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductRequest {
    // 고유값인 id는 제외함.
    private String name;
    private String description; // 세부설명, 상세설명을 의미함
    private BigDecimal price;
}
