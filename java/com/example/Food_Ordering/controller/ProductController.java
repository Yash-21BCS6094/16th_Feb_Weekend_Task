package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.ProductDTO;
import com.example.Food_Ordering.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productServices;

    // Creating Products
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productServices.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // Updating Product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productServices.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Getting Product
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        ProductDTO product = productServices.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Get all Products using pagination
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productServices.getAllProduct(pageable);
        return ResponseEntity.ok(products);
    }

    // Get all product using price sorted in required order
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/sorted")
    public ResponseEntity<Page<ProductDTO>> getProductSortedByPrice(@RequestParam("page") int page,
                                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> sortedProducts = productServices.getProductSortedByPrice(pageable);
        return ResponseEntity.ok(sortedProducts);
    }

    // get all product by this price range
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<ProductDTO>> getProductByPriceRange(
            @RequestParam("minPrice") Double minPrice,
            @RequestParam("maxPrice") Double maxPrice) {
        List<ProductDTO> filteredProducts = productServices.getProductByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(filteredProducts);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        productServices.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
