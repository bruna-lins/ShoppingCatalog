package com.catalog.prjCatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.catalog.prjCatalog.dto.ProductDTO;
import com.catalog.prjCatalog.entities.Category;
import com.catalog.prjCatalog.entities.Product;
import com.catalog.prjCatalog.repositories.CategoryRepository;
import com.catalog.prjCatalog.repositories.ProductRepository;
import com.catalog.prjCatalog.services.exceptions.DatabaseException;
import com.catalog.prjCatalog.services.exceptions.ResourceNotFoundException;
import com.catalog.prjCatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {

		// Preparation of variables
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));

		// Simulation of repositories
		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		when(repository.getOne(existingId)).thenReturn(product);
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(categoryRepository.getOne(existingId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	// FindAll Tests
//	@Test
//	public void findAllPagedShouldReturnPage() {
//		Pageable pageable = PageRequest.of(0, 10);
//
//		Page<ProductDTO> result = service.findAllPaged(pageable);
//
//		Assertions.assertNotNull(result);
//		verify(repository).findAll(pageable);
//	}

	// FindById Tests
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	// Insert Tests
	@Test
	public void insertShouldSaveDataWhenIdDoesNotExist() {
		ProductDTO result = service.insert(productDTO);
		
		Assertions.assertEquals(productDTO.getId(), result.getId());
		Assertions.assertEquals(productDTO.getName(), result.getName());
		Assertions.assertEquals(productDTO.getDescription(), result.getDescription());
		Assertions.assertEquals(productDTO.getPrice(), result.getPrice());
		Assertions.assertEquals(productDTO.getImgUrl(), result.getImgUrl());
		Assertions.assertEquals(productDTO.getDate(), result.getDate());
	}

	// Update Tests
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.update(existingId, productDTO);

		Assertions.assertNotNull(result);
	}

	@Test
	public void updateShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
	}

	// Delete Tests
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows((ResourceNotFoundException.class), () -> {
			service.delete(nonExistingId);
		});

		verify(repository, times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows((DatabaseException.class), () -> {
			service.delete(dependentId);
		});

		verify(repository, times(1)).deleteById(dependentId);
	}

}
