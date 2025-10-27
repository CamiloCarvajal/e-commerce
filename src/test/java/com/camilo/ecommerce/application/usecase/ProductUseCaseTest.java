package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Book;
import com.camilo.ecommerce.domain.model.Notebook;
import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.domain.model.ProductFilter;
import com.camilo.ecommerce.domain.repository.ProductRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductFilterDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Book testBook;
    private Notebook testNotebook;
    private ProductRequestDTO bookRequestDTO;
    private ProductRequestDTO notebookRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup test book
        testBook = new Book();
        testBook.setId(1);
        testBook.setName("Clean Code");
        testBook.setNumber_pages(464);
        testBook.setCost(49.99);
        testBook.setAuthor("Robert C. Martin");
        testBook.setLanguage("English");
        testBook.setTopic("Programming");

        // Setup test notebook
        testNotebook = new Notebook();
        testNotebook.setId(2);
        testNotebook.setName("College Ruled Notebook");
        testNotebook.setNumber_pages(100);
        testNotebook.setCost(5.99);
        testNotebook.setLineType("College Ruled");

        // Setup request DTO for book
        bookRequestDTO = new ProductRequestDTO();
        bookRequestDTO.setType("book");
        bookRequestDTO.setName("Clean Code");
        bookRequestDTO.setNumber_pages(464);
        bookRequestDTO.setCost(49.99);
        bookRequestDTO.setAuthor("Robert C. Martin");
        bookRequestDTO.setLanguage("English");
        bookRequestDTO.setTopic("Programming");

        // Setup request DTO for notebook
        notebookRequestDTO = new ProductRequestDTO();
        notebookRequestDTO.setType("notebook");
        notebookRequestDTO.setName("College Ruled Notebook");
        notebookRequestDTO.setNumber_pages(100);
        notebookRequestDTO.setCost(5.99);
        notebookRequestDTO.setLineType("College Ruled");
    }

    @Test
    void getAllProducts_WhenNoFilters_ShouldReturnAllProducts() {
        
        List<Product> products = Arrays.asList(testBook, testNotebook);
        when(productRepository.findAll()).thenReturn(products);

        
        List<ProductResponseDTO> result = productUseCase.getAllProducts(null);

        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getAllProducts_WhenHasFilters_ShouldCallFindWithFilters() {
        
        ProductFilterDTO filterDTO = new ProductFilterDTO();
        filterDTO.setName("code");
        filterDTO.setMinCost(10.0);
        filterDTO.setMaxCost(100.0);
        filterDTO.setSortBy("cost");
        filterDTO.setSortOrder("asc");

        List<Product> products = Arrays.asList(testBook);
        when(productRepository.findWithFilters(any(ProductFilter.class))).thenReturn(products);

        
        List<ProductResponseDTO> result = productUseCase.getAllProducts(filterDTO);

        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findWithFilters(any(ProductFilter.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        
        when(productRepository.findById(1)).thenReturn(Optional.of(testBook));

        
        Optional<ProductResponseDTO> result = productUseCase.getProductById(1);

        
        assertTrue(result.isPresent());
        assertEquals("Clean Code", result.get().getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturnEmpty() {
        
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        
        Optional<ProductResponseDTO> result = productUseCase.getProductById(999);

        
        assertTrue(result.isEmpty());
    }

    @Test
    void createProduct_WhenTypeIsBook_ShouldCreateBook() {
        
        when(productRepository.save(any(Book.class))).thenReturn(testBook);

        
        ProductResponseDTO result = productUseCase.createProduct(bookRequestDTO);

        
        assertNotNull(result);
        assertEquals("Clean Code", result.getName());
        assertEquals("book", result.getAuthor() != null ? "book" : null);
        verify(productRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createProduct_WhenTypeIsNotebook_ShouldCreateNotebook() {
        
        when(productRepository.save(any(Notebook.class))).thenReturn(testNotebook);

        
        ProductResponseDTO result = productUseCase.createProduct(notebookRequestDTO);

        
        assertNotNull(result);
        assertEquals("College Ruled Notebook", result.getName());
        assertEquals("College Ruled", result.getLineType());
        verify(productRepository, times(1)).save(any(Notebook.class));
    }

    @Test
    void createProduct_WhenInvalidType_ShouldThrowException() {
        
        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setType("invalid");

        
        assertThrows(IllegalArgumentException.class, () -> {
            productUseCase.createProduct(invalidRequest);
        });
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        
        ProductRequestDTO updateDTO = new ProductRequestDTO();
        updateDTO.setName("Updated Name");
        
        when(productRepository.findById(1)).thenReturn(Optional.of(testBook));
        when(productRepository.save(any(Product.class))).thenReturn(testBook);

        
        Optional<ProductResponseDTO> result = productUseCase.updateProduct(1, updateDTO);

        
        assertTrue(result.isPresent());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnEmpty() {
        
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        
        Optional<ProductResponseDTO> result = productUseCase.updateProduct(999, bookRequestDTO);

        
        assertTrue(result.isEmpty());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldCallRepository() {
        
        productUseCase.deleteProduct(1);

        
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void productExists_WhenProductExists_ShouldReturnTrue() {
        
        when(productRepository.existsById(1)).thenReturn(true);

        
        boolean result = productUseCase.productExists(1);

        
        assertTrue(result);
    }

    @Test
    void productExists_WhenProductNotExists_ShouldReturnFalse() {
        
        when(productRepository.existsById(999)).thenReturn(false);

        
        boolean result = productUseCase.productExists(999);

        
        assertFalse(result);
    }
}

