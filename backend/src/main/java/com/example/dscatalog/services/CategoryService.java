package com.example.dscatalog.services;

import com.example.dscatalog.dtos.CategoryDto;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAllPaged (Pageable pageable) {
        Page<Category> list = categoryRepository.findAll(pageable);
        return list.map(cat -> new CategoryDto(cat));
    }

    @Transactional(readOnly = true)
    public CategoryDto findById (Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto create (CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto update (CategoryDto categoryDto, Long id) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDto.getName());
            category = categoryRepository.save(category);
            return new CategoryDto(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity Not Found, Unable to UPDATE");
        }
    }


    public void delete (Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id not Found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation");
        }
     }

}
