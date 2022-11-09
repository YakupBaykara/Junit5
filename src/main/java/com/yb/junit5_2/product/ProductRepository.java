package com.yb.junit5_2.product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{

	Product findByName(String name);
}
