package org.example.hackathon_de01.controller;

import org.example.hackathon_de01.model.entity.Product;
import org.example.hackathon_de01.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
// Product Controller
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm"));
        return ResponseEntity.ok(product);
    }
}
