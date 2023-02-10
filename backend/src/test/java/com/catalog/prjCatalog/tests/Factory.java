package com.catalog.prjCatalog.tests;

import java.time.Instant;

import com.catalog.prjCatalog.dto.ProductDTO;
import com.catalog.prjCatalog.entities.Category;
import com.catalog.prjCatalog.entities.Product;

public class Factory {

	public static Product createProduct() { 
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2023-02-09T15:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() { 
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() { 
		return new Category(1L, "Electronics");
	}
}
