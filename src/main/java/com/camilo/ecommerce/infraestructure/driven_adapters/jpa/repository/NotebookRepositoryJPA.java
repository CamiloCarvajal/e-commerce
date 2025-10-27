package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotebookRepositoryJPA extends JpaRepository<NotebookEntity, Integer> {
    List<NotebookEntity> findByLineTypeContainingIgnoreCase(String lineType);
    
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

