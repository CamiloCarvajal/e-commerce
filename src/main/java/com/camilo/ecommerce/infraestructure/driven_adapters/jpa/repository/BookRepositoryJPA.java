package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepositoryJPA extends JpaRepository<BookEntity, Integer> {
    List<BookEntity> findByAuthorContainingIgnoreCase(String author);
    
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
}

