package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iphone-13
    // 여기서 iphone-13 이 sukCode이다. http요청하는모습이다.

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red
    // 여기서 iphone-13,iphone13-red 이 sukCode이다. 여러개를 http요청하는모습이다.

    // http://localhost:8082/api/inventory?sku-code=iphone-13&sku-code=iphone13-red
    // 이 형식이 조금더 쉬운 형식이다. 여기서 iphone-13,iphone13-red가 sku-code이다. 여러개를 http요청하는모습이다.
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        // Service의 결과값을 return 해준다.
        return inventoryService.isInStock(skuCode);
    }
}
