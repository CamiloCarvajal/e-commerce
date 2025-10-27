package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Book;
import com.camilo.ecommerce.domain.model.Notebook;
import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.domain.model.ProductFilter;
import com.camilo.ecommerce.domain.repository.ProductRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.ProductEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.BookRepositoryJPA;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.NotebookRepositoryJPA;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.ProductRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    @Autowired
    private ProductRepositoryJPA productRepositoryJPA;

    @Autowired
    private BookRepositoryJPA bookRepositoryJPA;

    @Autowired
    private NotebookRepositoryJPA notebookRepositoryJPA;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findAll() {
        return productRepositoryJPA.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(Integer id) {
        Optional<ProductEntity> entity = productRepositoryJPA.findById(id);
        return entity.map(productMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity saved;
        
        if (product instanceof Book) {
            saved = bookRepositoryJPA.save((BookEntity) entity);
        } else if (product instanceof Notebook) {
            saved = notebookRepositoryJPA.save((NotebookEntity) entity);
        } else {
            saved = productRepositoryJPA.save(entity);
        }
        
        return productMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        productRepositoryJPA.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return productRepositoryJPA.existsById(id);
    }

    @Override
    public List<Product> findWithFilters(ProductFilter filter) {
        List<ProductEntity> allProducts = new ArrayList<>();
        
        // If filter has author, search in books
        if (filter != null && filter.getAuthor() != null && !filter.getAuthor().isEmpty()) {
            List<BookEntity> books = bookRepositoryJPA.findBooksByFilters(
                filter.getName(),
                filter.getMinPages(),
                filter.getMaxPages(),
                filter.getMinCost(),
                filter.getMaxCost(),
                filter.getAuthor()
            );
            allProducts.addAll(books);
        }
        // If filter has lineType, search in notebooks
        else if (filter != null && filter.getLineType() != null && !filter.getLineType().isEmpty()) {
            List<NotebookEntity> notebooks = notebookRepositoryJPA.findNotebooksByFilters(
                filter.getName(),
                filter.getMinPages(),
                filter.getMaxPages(),
                filter.getMinCost(),
                filter.getMaxCost(),
                filter.getLineType()
            );
            allProducts.addAll(notebooks);
        }
        // Otherwise search all products
        else {
            allProducts = productRepositoryJPA.findByFilters(
                filter != null ? filter.getName() : null,
                filter != null ? filter.getMinPages() : null,
                filter != null ? filter.getMaxPages() : null,
                filter != null ? filter.getMinCost() : null,
                filter != null ? filter.getMaxCost() : null
            );
        }
        
        List<Product> products = allProducts.stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
        
        // Apply sorting if specified
        if (filter != null && filter.getSortBy() != null && !filter.getSortBy().isEmpty()) {
            Comparator<Product> comparator = null;
            
            if ("cost".equalsIgnoreCase(filter.getSortBy())) {
                comparator = Comparator.comparing(Product::getCost);
            } else if ("name".equalsIgnoreCase(filter.getSortBy())) {
                comparator = Comparator.comparing(Product::getName);
            } else if ("pages".equalsIgnoreCase(filter.getSortBy())) {
                comparator = Comparator.comparing(Product::getNumber_pages);
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
        
        return products;
    }
}

