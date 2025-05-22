package com.bookstore.server.core.repository;


import com.bookstore.server.core.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {


    Optional<Book> findByCode(String bookCode);

    @Query(value = "SELECT * FROM bookstore.Book b WHERE SIMILARITY(b.title, ?1) > 0.1 ORDER BY similarity(b.title, ?1) DESC", nativeQuery = true)
    List<Book> findByTitle(String title);

    @Query(value = "SELECT b.* FROM bookstore.book b " +
            "INNER JOIN bookstore.book_author ba ON b.id = ba.book_id " +
            "INNER JOIN bookstore.author a ON ba.author_id = a.id " +
            "WHERE SIMILARITY(a.name, ?1) > 0.1 ORDER BY similarity(a.name, ?1) DESC", nativeQuery = true)
    List<Book> findByAuthor(String author);

    @Query(value = "SELECT b.* FROM bookstore.book b " +
            "INNER JOIN bookstore.book_author ba ON b.id = ba.book_id " +
            "INNER JOIN bookstore.author a ON ba.author_id = a.id " +
            "WHERE SIMILARITY(b.title, ?1) > 0.1 and SIMILARITY(a.name, ?2) > 0.1 ORDER BY similarity(b.title, ?1) DESC", nativeQuery = true)
    List<Book> findByTitleAndAuthor(String title, String author);

    Optional<Book> findByTitleAndDateOfPublication(String title, LocalDate dateOfPublication);

}
