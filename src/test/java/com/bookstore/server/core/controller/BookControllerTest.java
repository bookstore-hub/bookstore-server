
package com.bookstore.server.core.controller;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.repository.AuthorRepository;
import com.bookstore.server.core.repository.BookRepository;
import com.bookstore.server.core.service.BookService;
import com.bookstore.server.core.to.BookTo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookService bookService;
    private BookRepository bookRepository;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        bookRepository = mock(BookRepository.class);
        bookController = new BookController(bookService, bookRepository);
    }

    @Test
    void createBook_createsNewBookSuccessfully() {
        BookTo.NewData bookData = new BookTo.NewData("Java Programming", LocalDate.now(), "A beautiful story", 200, List.of("John Doe"));
        String authorCode = "AUTHORCODE1";
        String authorName = "John Doe";
        Author author = new Author(authorCode, authorName);
        Book newBook = new Book("BOOKCODE1", "Java Programming", Set.of(author));

        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        BookTo.GetData result = bookController.addBook(bookData);

        assertNotNull(result);
        assertEquals("Java Programming", result.title());
    }

    @Test
    void editBook_editsExistingBookSuccessfully() {
        String bookCode = "BOOKCODE1";
        BookTo.NewData bookData = new BookTo.NewData("Advanced Java", LocalDate.now(), "A beautiful story", 200, List.of("John Doe"));
        String authorCode = "AUTHORCODE1";
        String authorName = "John Doe";
        Author author = new Author(authorCode, authorName);
        Book existingBook = new Book(bookCode, "Java Programming", Set.of(author));

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        BookTo.GetData result = bookController.editBook(bookCode, bookData);

        assertNotNull(result);
        assertEquals("Advanced Java", result.title());
        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void editBook_throwsExceptionWhenBookNotFound() {
        String bookCode = "CODE1";
        BookTo.NewData bookData = new BookTo.NewData("Java Programming", LocalDate.now(), "A beautiful story", 200, List.of("John Doe"));

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookController.editBook(bookCode, bookData));

        assertEquals("The book to edit with code CODE1 could not be found.", exception.getMessage());
        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void removeBook_removesBookSuccessfully() {
        String bookCode = "BOOKCODE1";
        String bookTitle = "Java Programming";
        String authorCode = "AUTHORCODE1";
        String authorName = "John Doe";
        Author author = new Author(authorCode, authorName);
        Book bookToRemove = new Book(bookCode, bookTitle, Set.of(author));

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.of(bookToRemove));

        bookController.removeBook(bookCode);

        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, times(1)).delete(bookToRemove);
    }

    @Test
    void removeBook_throwsExceptionWhenBookNotFound() {
        String bookCode = "CODE1";

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookController.removeBook(bookCode));

        assertEquals("The book to delete with code CODE1 could not be found.", exception.getMessage());
        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    void retrieveBookDetails_returnsBookDetailsSuccessfully() {
        String bookCode = "BOOKCODE1";
        String bookTitle = "Java Programming";
        String authorCode = "AUTHORCODE1";
        String authorName = "John Doe";
        Author author = new Author(authorCode, authorName);
        Book book = new Book(bookCode, bookTitle, Set.of(author));
        book.setDateOfPublication(LocalDate.now());

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.of(book));

        BookTo.GetData result = bookController.retrieveBookDetails(bookCode);

        assertNotNull(result);
        assertEquals("Java Programming", result.title());
        verify(bookRepository, times(1)).findByCode(bookCode);
    }

    @Test
    void retrieveBookDetails_throwsExceptionWhenBookNotFound() {
        String bookCode = "CODE1";

        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookController.retrieveBookDetails(bookCode));

        assertEquals("The book to retrieve with code CODE1 could not be found.", exception.getMessage());
        verify(bookRepository, times(1)).findByCode(bookCode);
    }

    @Test
    void searchBooks_returnsBooksSuccessfully() {
        String authorCode = "CODE1";
        String authorName = "John Doe";
        String bookTitle = "Java Programming";
        Author author = new Author(authorCode, authorName);
        Book book = new Book("CODE1", "Java Programming", Set.of(author));

        when(bookService.searchBooks(bookTitle, authorName)).thenReturn(List.of(book));

        List<BookTo.GetListedData> result = bookController.searchBooks(bookTitle, authorName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookService, times(1)).searchBooks(bookTitle, authorName);
    }

    @Test
    void searchBooks_returnsEmptyListWhenNoBooksFound() {
        String title = "Nonexistent Title";
        String author = "Unknown Author";

        when(bookService.searchBooks(title, author)).thenReturn(List.of());

        List<BookTo.GetListedData> result = bookController.searchBooks(title, author);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookService, times(1)).searchBooks(title, author);
    }

}