package com.example.dscatalog.resources;

import com.example.dscatalog.dtos.ProductDto;
import com.example.dscatalog.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll (Pageable pageable) {
        return ResponseEntity.ok().body(productService.findAllPaged(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> findById (@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create (@RequestBody ProductDto productDto) {
        productDto = productService.create(productDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> update (@RequestBody ProductDto productDto, @PathVariable Long id) {
        productDto = productService.update(productDto, id);
        return ResponseEntity.ok().body(productDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
