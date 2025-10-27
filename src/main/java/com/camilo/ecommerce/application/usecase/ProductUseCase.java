package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Book;
import com.camilo.ecommerce.domain.model.Notebook;
import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.domain.repository.ProductRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductFilterDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductUseCase {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponseDTO> getAllProducts(ProductFilterDTO filter) {
        List<Product> products = productRepository.findAll();
        
        // Apply filters
        if (filter != null) {
            products = products.stream()
                    .filter(p -> filter.getName() == null || p.getName().toLowerCase().contains(filter.getName().toLowerCase()))
                    .filter(p -> filter.getMinPages() == null || p.getNumber_pages() >= filter.getMinPages())
                    .filter(p -> filter.getMaxPages() == null || p.getNumber_pages() <= filter.getMaxPages())
                    .filter(p -> filter.getMinCost() == null || p.getCost() >= filter.getMinCost())
                    .filter(p -> filter.getMaxCost() == null || p.getCost() <= filter.getMaxCost())
                    .filter(p -> filter.getAuthor() == null || (p instanceof Book && ((Book) p).getAuthor().toLowerCase().contains(filter.getAuthor().toLowerCase())))
                    .filter(p -> filter.getLineType() == null || (p instanceof Notebook && ((Notebook) p).getLineType().toLowerCase().contains(filter.getLineType().toLowerCase())))
                    .collect(Collectors.toList());
            
            // Apply sorting
            if (filter.getSortBy() != null) {
                Comparator<Product> comparator = null;
                
                if (filter.getSortBy().equalsIgnoreCase("cost")) {
                    comparator = Comparator.comparing(Product::getCost);
                } else if (filter.getSortBy().equalsIgnoreCase("name")) {
                    comparator = Comparator.comparing(Product::getName);
                }
                
                if (comparator != null) {
                    if ("desc".equalsIgnoreCase(filter.getSortOrder())) {
                        comparator = comparator.reversed();
                    }
                    products = products.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
            }
        }
        
        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
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
