package com.programmingtechie.productservice.respository;


import com.programmingtechie.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

// Product에 있는 데이터들을 저장함.
public interface ProductRepository extends MongoRepository<Product,String> {

}
