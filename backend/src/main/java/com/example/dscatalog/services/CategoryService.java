package com.example.dscatalog.services;

import com.example.dscatalog.dtos.CategoryDto;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
