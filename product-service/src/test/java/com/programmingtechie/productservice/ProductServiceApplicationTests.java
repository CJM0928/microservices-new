package com.programmingtechie.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.respository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// java.lang.IllegalStateException: Could not find a valid Docker environment. Please see logs and check configuration
// 이라는 오류가 나오면, 도커를 실행해줘야 하므로 데스크탑에서 도커를 실행해주자

// 말그대로 main 디렉토리에서 만든 서비스들을 테스트하는 파일
// 단위테스트, 통합테스트 등이 있다.
// 여기서는 TestContainers 를 사용하겠다. ( 통합테스트 )
// TestContainers는 데이터베이스 웹과 같이 필요한 공통 소프트웨어의 일회용 인스턴스를 제공하여
// junit 테스트를 작성할 수 있도록 도와주는 Java 라이브러리다.
// TestContainer에 사용되는 모든 종속성은 동일한 버전을 사용하는 BOM 을갖는것이 좋다.

// 작동은 어떻게 이루어지냐,
// 우선, 테스트를 시작할때 mongodb의 컨테이너를 시작하고 mongodb의 버전은 4.4.2고,
// 복제한(레플리카)uri을 가져와서 테스트를 생성할때 동적으로 스프링데이터 mongodb uri속성을 추가한다.
// application.properties는 수동으로 uri를 제공하는 반면
// 여기 테스트는, mongouri를 동적으로 제공해야한다. localhost를 사용하지 않기 때문이다. -> @DynamicPropertySource 이 애너테이션 사용

// 첫번째로 product의 controller를 위한 endpoint 생성이다. POST를 하게되면 올바르게 생성되는지 테스트하는것.
// 여기서는 mock을 사용할것이고, 모의 mvc라고 생각하면된다. ( mock 테스트 같은 용어임. )
// 그다음에 생성될때 요청메세지가 201을 출력하는지 확인하는것.

// 이제, 나머지 테스트들 ( 주문 서비스, 재고 서비스등은 ) 깃허브에가서 참고

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc	// MockMvc mockMvc 인스턴스를 자동으로 설	정하도록 Spring에 지시 즉, mockMvc의 오류가 사라짐
class ProductServiceApplicationTests {

	// mongodb의 버전을 수동으로 지정해야 하는데,
	// dockerImageName은 내가 테스트하고싶은 mongodb의 버전이다.
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	// objectMapper는 말그대로, pojo 개체를 json으로 또는 그반대로 변환하는데 사용
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		// 여기서 해야할일은 productrequest 객체를 json을 이용해서 문자열로 변환한다
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iphone 13")
				.description("iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
}
