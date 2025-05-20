package com.bookstore.server.core.service;


import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.repository.BookRepository;
import info.debatty.java.stringsimilarity.Levenshtein;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.comparing;


/**
 * @author david.garcia
 */
@Service
public class BookServiceImpl implements BookService {

    private static final int MAX_RESULTS = 5;

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> searchBooks(String title, String author) {
        List<Book> booksRetrieved = List.of();
        List<Book> booksSorted;

        if(StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author)) {
            booksRetrieved = bookRepository.findByTitleAndAuthor(title, author);
        } else if(StringUtils.isNotBlank(title)) {
            booksRetrieved = bookRepository.findByTitle(title);
        }else if (StringUtils.isNotBlank(author)){
            booksRetrieved = bookRepository.findByAuthor(author);
        }

        booksSorted = booksRetrieved.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getTitle(), title)))
                .limit(MAX_RESULTS).toList();

        return booksSorted;
    }

}
