package com.bookstore.server.core.controller;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.exception.PreconditionFailedException;
import com.bookstore.server.core.repository.AuthorRepository;
import com.bookstore.server.core.repository.BookRepository;
import com.bookstore.server.core.to.AuthorTo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        bookRepository = mock(BookRepository.class);
        authorController = new AuthorController(authorRepository, bookRepository);
    }

    @Test
    void createAuthor_createsNewAuthorSuccessfully() {
        String authorCode = "CODE1";
        String authorName = "John Doe";
        Author newAuthor = new Author(authorCode, authorName);

        when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

        AuthorTo.GetData result = authorController.createAuthor(authorName);

        assertNotNull(result);
        assertEquals(authorName, result.name());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void editAuthor_editsExistingAuthorSuccessfully() {
        String authorCode = "CODE1";
        String newAuthorName = "Jane Doe";
        Author existingAuthor = new Author(authorCode, "John Doe");

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);

        AuthorTo.GetData result = authorController.editAuthor(authorCode, newAuthorName);

        assertNotNull(result);
        assertEquals(newAuthorName, result.name());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void editAuthor_throwsExceptionWhenAuthorNotFound() {
        String authorCode = "CODE1";
        String newAuthorName = "Jane Doe";

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authorController.editAuthor(authorCode, newAuthorName));

        assertEquals("The author to edit with code CODE1 could not be found.", exception.getMessage());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void removeAuthor_removesAuthorSuccessfully() {
        String authorCode = "CODE1";
        String authorName = "John Doe";
        Author authorToRemove = mock(Author.class);
        authorToRemove.setCode(authorCode);
        authorToRemove.setName(authorName);

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.of(authorToRemove));
        when(authorToRemove.hasBookWithOneAuthorOnly()).thenReturn(false);

        authorController.removeAuthor(authorCode);

        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(authorRepository, times(1)).delete(authorToRemove);
    }

    @Test
    void removeAuthor_throwsExceptionWhenAuthorNotFound() {
        String authorCode = "CODE1";

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authorController.removeAuthor(authorCode));

        assertEquals("The author to delete with code CODE1 could not be found.", exception.getMessage());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(authorRepository, never()).delete(any(Author.class));
    }

    @Test
    void removeAuthor_throwsPreconditionFailedExceptionWhenAuthorHasBooks() {
        String authorCode = "CODE1";
        String authorName = "John Doe";
        Author authorToRemove = mock(Author.class);
        authorToRemove.setCode(authorCode);
        authorToRemove.setName(authorName);

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.of(authorToRemove));
        when(authorToRemove.hasBookWithOneAuthorOnly()).thenReturn(true);

        PreconditionFailedException exception = assertThrows(PreconditionFailedException.class, () -> authorController.removeAuthor(authorCode));

        assertEquals("The author can't be deleted because each book must have at least one author.", exception.getMessage());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(authorRepository, never()).delete(any(Author.class));
    }

    @Test
    void addAuthorToBook_throwsExceptionWhenAuthorOrBookNotFound() {
        String authorCode = "CODE1";
        String bookCode = "BOOK1";

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.empty());
        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authorController.addAuthorToBook(authorCode, bookCode));

        assertEquals("Author could not be added to book. Either the author CODE1 or the book BOOK1 could not be found.", exception.getMessage());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void removeAuthorFromBook_throwsExceptionWhenAuthorOrBookNotFound() {
        String authorCode = "CODE1";
        String bookCode = "BOOK1";

        when(authorRepository.findByCode(authorCode)).thenReturn(Optional.empty());
        when(bookRepository.findByCode(bookCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authorController.removeAuthorFromBook(authorCode, bookCode));

        assertEquals("Author could not be removed from book. Either the author CODE1 or the book BOOK1 could not be found.", exception.getMessage());
        verify(authorRepository, times(1)).findByCode(authorCode);
        verify(bookRepository, times(1)).findByCode(bookCode);
        verify(bookRepository, never()).save(any(Book.class));
    }

}