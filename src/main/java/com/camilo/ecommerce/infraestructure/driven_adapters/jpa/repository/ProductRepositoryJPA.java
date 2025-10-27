package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepositoryJPA extends JpaRepository<ProductEntity, Integer> {
    
    @Query("SELECT p FROM product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:minPages IS NULL OR p.number_pages >= :minPages) AND " +
           "(:maxPages IS NULL OR p.number_pages <= :maxPages) AND " +
           "(:minCost IS NULL OR p.cost >= :minCost) AND " +
           "(:maxCost IS NULL OR p.cost <= :maxCost)")
    List<ProductEntity> findByFilters(@Param("name") String name,
                                       @Param("minPages") Integer minPages,
                                       @Param("maxPages") Integer maxPages,
                                       @Param("minCost") Double minCost,
                                       @Param("maxCost") Double maxCost);
    
    @Query("SELECT b FROM book b WHERE " +
           "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:minPages IS NULL OR b.number_pages >= :minPages) AND " +
           "(:maxPages IS NULL OR b.number_pages <= :maxPages) AND " +
           "(:minCost IS NULL OR b.cost >= :minCost) AND " +
           "(:maxCost IS NULL OR b.cost <= :maxCost) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))")
    List<BookEntity> findBooksByFilters(@Param("name") String name,
                                        @Param("minPages") Integer minPages,
                                        @Param("maxPages") Integer maxPages,
                                        @Param("minCost") Double minCost,
                                        @Param("maxCost") Double maxCost,
                                        @Param("author") String author);
    
    @Query("SELECT n FROM notebook n WHERE " +
           "(:name IS NULL OR LOWER(n.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:minPages IS NULL OR n.number_pages >= :minPages) AND " +
           "(:maxPages IS NULL OR n.number_pages <= :maxPages) AND " +
           "(:minCost IS NULL OR n.cost >= :minCost) AND " +
           "(:maxCost IS NULL OR n.cost <= :maxCost) AND " +
           "(:lineType IS NULL OR LOWER(n.lineType) LIKE LOWER(CONCAT('%', :lineType, '%')))")
    List<NotebookEntity> findNotebooksByFilters(@Param("name") String name,
                                                 @Param("minPages") Integer minPages,
                                                 @Param("maxPages") Integer maxPages,
                                                 @Param("minCost") Double minCost,
                                                 @Param("maxCost") Double maxCost,
                                                 @Param("lineType") String lineType);
}

