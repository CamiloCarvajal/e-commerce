package com.camilo.ecommerce.infraestructure.entry_points.rest;

import com.camilo.ecommerce.application.usecase.ProductUseCase;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductFilterDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductUseCase productUseCase;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Double minCost,
            @RequestParam(required = false) Double maxCost,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String lineType,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        
        ProductFilterDTO filter = new ProductFilterDTO();
        filter.setName(name);
        filter.setMinPages(minPages);
        filter.setMaxPages(maxPages);
        filter.setMinCost(minCost);
        filter.setMaxCost(maxCost);
        filter.setAuthor(author);
        filter.setLineType(lineType);
        filter.setSortBy(sortBy);
        filter.setSortOrder(sortOrder);
        
        List<ProductResponseDTO> products = productUseCase.getAllProducts(filter);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {
        Optional<ProductResponseDTO> product = productUseCase.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        try {
            ProductResponseDTO created = productUseCase.createProduct(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        Optional<ProductResponseDTO> updated = productUseCase.updateProduct(id, requestDTO);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productUseCase.productExists(id)) {
            productUseCase.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

