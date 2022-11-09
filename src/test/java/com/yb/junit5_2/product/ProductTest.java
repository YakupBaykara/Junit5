package com.yb.junit5_2.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@AutoConfigureTestDatabase(replace = Replace.NONE)				 // Eğer MySql'de işlemleri yapmak istiyorsak
//@TestPropertySource(locations = "classpath.test.properties")		 // Eğer istenirse test.properties dosyası oluşturulup onun adresi de verilebilir.
																	 // Böylece aşağıdaki gibi Rollback(false) yapmaya gerek kalmaz.	
public class ProductTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	@Order(1)
	@Rollback(false) // MySql' de kayıt edebilmek için yapılır.
	public void testProductCreate() {
		Product product = new Product("iPhone 10", 789);
		Product savedProduct = productRepository.save(product);
		
		assertNotNull(savedProduct);		// assertThat(product).isNotNull();
		assertTrue(product.getId() > 0);	// assertThat(product.getId()).isGreaterThan(0);
	}
	
	@Test
	@Order(2)
	public void testFindProductByNameExist() {
		String name = "iPhone 10";
		Product product = productRepository.findByName(name);
		
		assertThat(product.getName()).isEqualTo(name);
	}
	
	@Test
	@Order(3)
	public void testFindProductByNameNotExist() {
		String name = "iPhone 11";
		Product product = productRepository.findByName(name);
		
		assertNull(product);
	}
	
	@Test
	@Order(4)
	public void testListProducts() {
		List<Product> productsList = (List<Product>) productRepository.findAll();
		
		assertThat(productsList).size().isGreaterThan(0);
	}
	
	@Test
	@Order(5)
	@Rollback(false)
	public void testUpdateProduct() {
		String productName = "Kindle Reader";
		Product product = new Product(productName, 199);
		product.setId(1);	// MySql' de iPhone ait id olacak
		productRepository.save(product);
		
		Product updatedProduct = productRepository.findByName(productName);
		
		assertThat(updatedProduct.getName()).isEqualTo(productName);
	}
	
	@Test
	@Order(6)
	@Rollback(false)
	public void testDeleteProduct() {
		Integer id = 1;
		
		boolean isExistBeforeDelete = productRepository.findById(id).isPresent();
		productRepository.deleteById(id);
		boolean notExistAfterDelete = productRepository.findById(id).isPresent();
		
		assertTrue(isExistBeforeDelete);
		assertFalse(notExistAfterDelete);
		
	}
}
