package com.bookstore.server.core.repository;


import com.bookstore.server.core.entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Optional<Author> findByCode(String authorCode);

    Optional<Author> findByName(String author);

}
