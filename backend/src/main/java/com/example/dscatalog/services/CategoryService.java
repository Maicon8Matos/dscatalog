package com.example.dscatalog.services;

import com.example.dscatalog.dtos.CategoryDto;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepsitory;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll () {
        List<Category> list = categoryRepsitory.findAll();
        return list.stream().map(cat -> new CategoryDto(cat)).toList();
    }

}
