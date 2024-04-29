package com.programmingtechie.productservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
// 주로 Spring Data MongoDB 에서 사용한다.
// value 속성은 이 문서가 매핑될 MongoDB 컬렉션의 이름을 지정한다.. 이 경우 "product" 컬렉션에 매핑된다..
@AllArgsConstructor
// 이 애너테이션은 Lombok 프로젝트에서 제공하는 기능 중 하나
// 클래스의 모든 필드를 사용하여 생성자를 자동으로 생성 즉,모든 필드를 인자로 받는 생성자가 생성된다.
@NoArgsConstructor
// 마찬가지로 Lombok에서 제공하는 기능 중 하나로, 매개변수가 없는 기본 생성자를 자동으로 생성
@Builder
// Lombok 애너테이션으로, 빌더 패턴을 자동으로 생성
// 빌더 패턴은 객체 생성시에 많은 파라미터를 편리하게 다룰 수 있도록 도와주는 디자인 패턴
@Data
// Lombok에서 제공하는 애너테이션으로,
// 클래스 내의 모든 필드에 대한 게터와 세터, toString(), equals(), hashCode() 메서드 등을 자동으로 생성

public class Product {
    @Id
    // 필드 id에 id 주석을 추가한다. 즉, id는 고유 식별자가 된다
    // 이 데이터를 저장하려면 스프링 데이터 저장소를 만들어야한다.
    private String id;
    private String name;
    private String description; // 세부설명, 상세설명을 의미함
    private BigDecimal price;
    // BigDecimal은 부동 소수점 연산에서 발생할 수 있는 정확도 문제를 피하기 위해 사용
    // 제품 객체의 경우 제품의 가격을 저장한다.
}
