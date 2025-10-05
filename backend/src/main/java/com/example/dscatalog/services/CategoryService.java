package com.example.dscatalog.services;

import com.example.dscatalog.dtos.CategoryDto;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll () {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(cat -> new CategoryDto(cat)).toList();
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
    

}
