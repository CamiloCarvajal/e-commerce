package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.domain.repository.ProductRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.ProductEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.BookRepositoryJPA;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.NotebookRepositoryJPA;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.ProductRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        
        if (product instanceof com.camilo.ecommerce.domain.model.Book) {
            saved = bookRepositoryJPA.save((BookEntity) entity);
        } else if (product instanceof com.camilo.ecommerce.domain.model.Notebook) {
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
}

