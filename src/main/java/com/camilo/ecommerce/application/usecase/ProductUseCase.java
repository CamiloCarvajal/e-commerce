package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Book;
import com.camilo.ecommerce.domain.model.Notebook;
import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.domain.model.ProductFilter;
import com.camilo.ecommerce.domain.repository.ProductRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductFilterDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductUseCase {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponseDTO> getAllProducts(ProductFilterDTO filterDTO) {
        List<Product> products;
        
        // Convert DTO to domain model
        ProductFilter filter = convertDTOToDomainFilter(filterDTO);
        
        // Use JPQL query if filters are provided
        if (filter != null && hasAnyFilter(filter)) {
            products = productRepository.findWithFilters(filter);
        } else {
            // No filters, get all products
            products = productRepository.findAll();
        }
        
        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private ProductFilter convertDTOToDomainFilter(ProductFilterDTO filterDTO) {
        if (filterDTO == null) return null;
        
        ProductFilter filter = new ProductFilter();
        filter.setName(filterDTO.getName());
        filter.setMinPages(filterDTO.getMinPages());
        filter.setMaxPages(filterDTO.getMaxPages());
        filter.setMinCost(filterDTO.getMinCost());
        filter.setMaxCost(filterDTO.getMaxCost());
        filter.setAuthor(filterDTO.getAuthor());
        filter.setLineType(filterDTO.getLineType());
        filter.setSortBy(filterDTO.getSortBy());
        filter.setSortOrder(filterDTO.getSortOrder());
        
        return filter;
    }
    
    private boolean hasAnyFilter(ProductFilter filter) {
        if (filter == null) return false;
        
        return (filter.getName() != null && !filter.getName().isEmpty()) ||
               filter.getMinPages() != null ||
               filter.getMaxPages() != null ||
               filter.getMinCost() != null ||
               filter.getMaxCost() != null ||
               (filter.getAuthor() != null && !filter.getAuthor().isEmpty()) ||
               (filter.getLineType() != null && !filter.getLineType().isEmpty());
    }

    public Optional<ProductResponseDTO> getProductById(Integer id) {
        return productRepository.findById(id).map(this::convertToResponseDTO);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = convertFromRequestDTO(requestDTO);
        Product saved = productRepository.save(product);
        return convertToResponseDTO(saved);
    }

    public Optional<ProductResponseDTO> updateProduct(Integer id, ProductRequestDTO requestDTO) {
        return productRepository.findById(id).map(existingProduct -> {
            updateProductFromDTO(existingProduct, requestDTO);
            Product updated = productRepository.save(existingProduct);
            return convertToResponseDTO(updated);
        });
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public boolean productExists(Integer id) {
        return productRepository.existsById(id);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setNumber_pages(product.getNumber_pages());
        dto.setCost(product.getCost());
        
        if (product instanceof Book) {
            Book book = (Book) product;
            dto.setAuthor(book.getAuthor());
            dto.setLanguage(book.getLanguage());
            dto.setTopic(book.getTopic());
        } else if (product instanceof Notebook) {
            Notebook notebook = (Notebook) product;
            dto.setLineType(notebook.getLineType());
        }
        
        return dto;
    }

    private Product convertFromRequestDTO(ProductRequestDTO requestDTO) {
        if ("book".equalsIgnoreCase(requestDTO.getType())) {
            Book book = new Book();
            book.setName(requestDTO.getName());
            book.setNumber_pages(requestDTO.getNumber_pages());
            book.setCost(requestDTO.getCost());
            book.setAuthor(requestDTO.getAuthor());
            book.setLanguage(requestDTO.getLanguage());
            book.setTopic(requestDTO.getTopic());
            return book;
        } else if ("notebook".equalsIgnoreCase(requestDTO.getType())) {
            Notebook notebook = new Notebook();
            notebook.setName(requestDTO.getName());
            notebook.setNumber_pages(requestDTO.getNumber_pages());
            notebook.setCost(requestDTO.getCost());
            notebook.setLineType(requestDTO.getLineType());
            return notebook;
        }
        
        throw new IllegalArgumentException("Product type must be either 'book' or 'notebook'");
    }

    private void updateProductFromDTO(Product product, ProductRequestDTO requestDTO) {
        if (requestDTO.getName() != null) {
            product.setName(requestDTO.getName());
        }
        if (requestDTO.getNumber_pages() != null) {
            product.setNumber_pages(requestDTO.getNumber_pages());
        }
        if (requestDTO.getCost() != null) {
            product.setCost(requestDTO.getCost());
        }
        
        if (product instanceof Book && requestDTO instanceof ProductRequestDTO) {
            Book book = (Book) product;
            if (requestDTO.getAuthor() != null) {
                book.setAuthor(requestDTO.getAuthor());
            }
            if (requestDTO.getLanguage() != null) {
                book.setLanguage(requestDTO.getLanguage());
            }
            if (requestDTO.getTopic() != null) {
                book.setTopic(requestDTO.getTopic());
            }
        } else if (product instanceof Notebook) {
            Notebook notebook = (Notebook) product;
            if (requestDTO.getLineType() != null) {
                notebook.setLineType(requestDTO.getLineType());
            }
        }
    }
}
