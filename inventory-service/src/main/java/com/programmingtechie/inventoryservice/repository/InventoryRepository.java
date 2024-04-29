package com.programmingtechie.inventoryservice.repository;

import com.programmingtechie.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 이 코드를 사용하게되면 Error creating bean with name 'inventoryController' 에러가 발생한다.
    // Optional 타입대신 List를 쓰자
    //Optional<Inventory> findBySkuCode();
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
