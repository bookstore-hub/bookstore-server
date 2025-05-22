package com.bookstore.server.core.service;


import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;

import java.util.List;

/**
 * @author david.garcia
 */
public interface BookService {

    List<Author> handleAuthors(List<String> authors);

    List<Book> searchBooks(String title, String author);

}
