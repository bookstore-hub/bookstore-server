package com.bookstore.server.core.service;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookRepository bookRepository;
    private BookServiceImpl bookService;

    Author author1;
    Author author2;
    Book book1;
    Book book2;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);

        author1 = new Author("John Doe");
        author2 = new Author("Jane Smith");
        book1 = new Book("CODE1", "Java Programming", Set.of(author1));
        book2 = new Book("CODE2", "Advanced C#", Set.of(author2));
    }

    @Test
    void searchBooks_withTitleAndAuthor() {
        String title = "Java Programming";
        String author = "John Doe";

        when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(List.of(book1, book2));

        List<Book> result = bookService.searchBooks(title, author);

        assertEquals(2, result.size());
        assertEquals(book1, result.getFirst());
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author);
    }

    @Test
    void searchBooks_withTitleOnly() {
        String title = "Java Programming";

        when(bookRepository.findByTitle(title)).thenReturn(List.of(book1));

        List<Book> result = bookService.searchBooks(title, null);

        assertEquals(1, result.size());
        assertEquals(book1, result.getFirst());
        verify(bookRepository, times(1)).findByTitle(title);
    }

    @Test
    void searchBooks_withAuthorOnly() {
        String author = "Jane Smith";

        when(bookRepository.findByAuthor(author)).thenReturn(List.of(book2));

        List<Book> result = bookService.searchBooks(null, author);

        assertEquals(1, result.size());
        assertEquals(book2, result.getFirst());
        verify(bookRepository, times(1)).findByAuthor(author);
    }

    @Test
    void searchBooks_withNoCriteria() {
        List<Book> result = bookService.searchBooks(null, null);

        assertEquals(0, result.size());
        verifyNoInteractions(bookRepository);
    }

}