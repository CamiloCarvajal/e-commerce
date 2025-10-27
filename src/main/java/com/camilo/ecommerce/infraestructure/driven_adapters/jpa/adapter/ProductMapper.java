package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Book;
import com.camilo.ecommerce.domain.model.Notebook;
import com.camilo.ecommerce.domain.model.Product;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(Product product) {
        if (product == null) return null;

        if (product instanceof Book) {
            BookEntity bookEntity = new BookEntity();
            Book book = (Book) product;
            bookEntity.setId(book.getId());
            bookEntity.setName(book.getName());
            bookEntity.setNumber_pages(book.getNumber_pages());
            bookEntity.setCost(book.getCost());
            bookEntity.setAuthor(book.getAuthor());
            bookEntity.setLanguage(book.getLanguage());
            bookEntity.setTopic(book.getTopic());
            return bookEntity;
        } else if (product instanceof Notebook) {
            NotebookEntity notebookEntity = new NotebookEntity();
            Notebook notebook = (Notebook) product;
            notebookEntity.setId(notebook.getId());
            notebookEntity.setName(notebook.getName());
            notebookEntity.setNumber_pages(notebook.getNumber_pages());
            notebookEntity.setCost(notebook.getCost());
            notebookEntity.setLineType(notebook.getLineType());
            return notebookEntity;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setNumber_pages(product.getNumber_pages());
        entity.setCost(product.getCost());
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        if (entity instanceof BookEntity) {
            BookEntity bookEntity = (BookEntity) entity;
            Book book = new Book();
            book.setId(bookEntity.getId());
            book.setName(bookEntity.getName());
            book.setNumber_pages(bookEntity.getNumber_pages());
            book.setCost(bookEntity.getCost());
            book.setAuthor(bookEntity.getAuthor());
            book.setLanguage(bookEntity.getLanguage());
            book.setTopic(bookEntity.getTopic());
            return book;
        } else if (entity instanceof NotebookEntity) {
            NotebookEntity notebookEntity = (NotebookEntity) entity;
            Notebook notebook = new Notebook();
            notebook.setId(notebookEntity.getId());
            notebook.setName(notebookEntity.getName());
            notebook.setNumber_pages(notebookEntity.getNumber_pages());
            notebook.setCost(notebookEntity.getCost());
            notebook.setLineType(notebookEntity.getLineType());
            return notebook;
        }

        return null;
    }
}

