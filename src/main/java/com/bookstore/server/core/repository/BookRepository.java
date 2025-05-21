package com.bookstore.server.core.repository;


import com.bookstore.server.core.entity.Book;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {


    Optional<Book> findByCode(String bookCode);

    @Query(value = "SELECT * FROM Book b inner join Author a WHERE SIMILARITY(b.title, ?1) > 0.7 and SIMILARITY(a.name, ?1) > 0.7", nativeQuery = true)
    List<Book> findByTitleAndAuthor(String title, String author);

    @Query(value = "SELECT * FROM Book b WHERE SIMILARITY(b.title, ?1) > 0.7", nativeQuery = true)
    List<Book> findByTitle(String title);

    @Query(value = "SELECT * FROM Book b inner join Author a WHERE SIMILARITY(a.name, ?1) > 0.7", nativeQuery = true)
    List<Book> findByAuthor(String author);

    Optional<Book> findByTitleAndDateOfPublication(String title, LocalDate dateOfPublication);

}
