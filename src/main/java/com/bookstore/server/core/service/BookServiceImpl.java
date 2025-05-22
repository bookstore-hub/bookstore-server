package com.bookstore.server.core.service;


import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.repository.AuthorRepository;
import com.bookstore.server.core.repository.BookRepository;
import com.bookstore.server.core.to.AuthorTo;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author david.garcia
 */
@Service
public class BookServiceImpl implements BookService {

    private static final int MAX_RESULTS = 5;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> handleAuthors(List<String> authorsToAdd) {
        List<Author> authors = new ArrayList<>();

        for(String authorToAdd : authorsToAdd) {
            Optional<Author> existingAuthor = authorRepository.findByName(authorToAdd);
            if(existingAuthor.isPresent()) {
                authors.add(existingAuthor.get());
            } else {
                Author newAuthor = AuthorTo.mapNewAuthor(authorToAdd);
                authorRepository.save(newAuthor);
                authors.add(newAuthor);
            }
        }

        return authors;
    }

    @Override
    public List<Book> searchBooks(String title, String author) {
        List<Book> books;
        List<Book> booksRetrieved = new ArrayList<>();

        if(StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author)) {
            booksRetrieved = bookRepository.findByTitleAndAuthor(title, author);
        } else if(StringUtils.isNotBlank(title)) {
            booksRetrieved = bookRepository.findByTitle(title);
        } else if (StringUtils.isNotBlank(author)){
            booksRetrieved = bookRepository.findByAuthor(author);
        }

        books = booksRetrieved.stream().distinct().limit(MAX_RESULTS).toList();

        return books;
    }

}
