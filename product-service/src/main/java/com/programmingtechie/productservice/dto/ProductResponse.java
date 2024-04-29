package com.programmingtechie.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

// 요청을하면, 받는 응답도 필요하다.
public class ProductResponse {
    // 필드 id에 id 주석을 추가한다. 즉, id는 고유 식별자가 된다
    // 이 데이터를 저장하려면 스프링 데이터 저장소를 만들어야한다.
    private String id;
    private String name;
    private String description; // 세부설명, 상세설명을 의미함
    private BigDecimal price;
}
