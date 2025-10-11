package com.example.dscatalog.services;

import com.example.dscatalog.dtos.CategoryDto;
import com.example.dscatalog.dtos.ProductDto;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.ProductRepository;
import com.example.dscatalog.services.exceptions.DatabaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged (Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(cat -> new ProductDto(cat));
    }

    @Transactional(readOnly = true)
    public ProductDto findById (Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
        return new ProductDto(product, product.getCategories());
    }

    @Transactional
    public ProductDto create (ProductDto productDto){
        Product product = new Product();
        copyDtoToEntity(productDto, product);
        product = productRepository.save(product);
        return new ProductDto(product);
    }

    @Transactional
    public ProductDto update (ProductDto productDto, Long id) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(productDto, product);
            product = productRepository.save(product);
            return new ProductDto(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity Not Found, Unable to UPDATE");
        }
    }

    public void delete (Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id not Found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation");
        }
     }


     private void copyDtoToEntity (ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());

        entity.getCategories().clear();
        for (CategoryDto catDto: dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
     }
}
