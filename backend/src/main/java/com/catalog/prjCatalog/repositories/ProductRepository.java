package com.catalog.prjCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalog.prjCatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
