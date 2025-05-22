package com.bookstore.server.core.controller;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.repository.AuthorRepository;
import com.bookstore.server.core.repository.BookRepository;
import com.bookstore.server.core.service.BookService;
import com.bookstore.server.core.to.AuthorTo;
import com.bookstore.server.core.to.BookTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/book")
@Tag(name = "BookController", description = "Set of endpoints to handle the Bookstore book logic")
public class BookController {

    protected static final Log LOGGER = LogFactory.getLog(BookController.class);

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookService bookService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }


    /**
     * Adds a new book
     *
     * @param bookData    the book data
     */
    @Operation(description = "Adds a new book")
    @PostMapping
    public BookTo.GetData addBook(@Parameter(description = "The book data") @RequestBody BookTo.NewData bookData) {
        Optional<Book> existingBook = bookRepository.findByTitleAndDateOfPublication(bookData.title(), bookData.dateOfPublication());
        if(existingBook.isPresent()) {
            throw new EntityExistsException("The book " + bookData.title() + " is already present in the database.");
        }

        LOGGER.info("Creating book " + bookData.title());

        Book newBook = BookTo.mapNewBook(bookData);
        List<Author> authors = handleAuthors(bookData.authors());
        addBook(newBook, authors);

        return new BookTo.GetData(newBook);
    }

    private List<Author> handleAuthors(List<String> authorsToAdd) {
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

    private void addBook(Book newBook, List<Author> authors) {
        for(Author author : authors) {
            newBook.addAuthor(author);
            bookRepository.save(newBook);
        }
    }

    /**
     * Edits a book entry
     *
     * @param bookCode     the book code
     */
    @Operation(description = "Edits a book entry")
    @PutMapping
    public BookTo.GetData editBook(@Parameter(description = "The book code") @RequestParam String bookCode,
                                   @Parameter(description = "The book data") @RequestBody BookTo.NewData bookData) {

        Optional<Book> bookToEdit = bookRepository.findByCode(bookCode);
        if(bookToEdit.isPresent()) {
            Book book = bookToEdit.get();

            LOGGER.info("Editing book entry for " + book.getTitle());
            BookTo.mapUpdatedBook(book, bookData);
            bookRepository.save(book);
            return new BookTo.GetData(book);
        } else {
            throw new EntityNotFoundException("The book to edit with code " + bookCode + " could not be found.");
        }

    }

    /**
     * Removes a book
     *
     * @param bookCode    the book code
     */
    @Operation(description = "Removes a book")
    @DeleteMapping
    public void removeBook(@Parameter(description = "The book code") @RequestParam String bookCode) {
        Optional<Book> bookToRemove = bookRepository.findByCode(bookCode);
        if(bookToRemove.isPresent()) {

            LOGGER.info("Removing book " + bookToRemove.get().getTitle());
            bookRepository.delete(bookToRemove.get());
        } else {
            throw new EntityNotFoundException("The book to delete with code " + bookCode + " could not be found.");
        }
    }

    /**
     * Retrieves a book details
     *
     * @param bookCode      the book code
     */
    @Operation(description = "Retrieve a book details")
    @GetMapping
    public BookTo.GetData retrieveBookDetails(@Parameter(description = "The book code") @RequestParam String bookCode) {
        BookTo.GetData bookDetails;
        Optional<Book> bookToRetrieve = bookRepository.findByCode(bookCode);
        if(bookToRetrieve.isPresent()) {

            LOGGER.info("Retrieving details of book " + bookToRetrieve.get().getTitle());
            bookDetails = new BookTo.GetData(bookToRetrieve.get());
            return bookDetails;
        } else {
            throw new EntityNotFoundException("The book to retrieve with code " + bookCode + " could not be found.");
        }


    }

    /**
     * Retrieve all books for a specific title or author
     *
     * @param title        the title
     * @param author       the author
     */
    @Operation(description = "Retrieve all books for a specific title or author")
    @GetMapping(value = "/search")
    public List<BookTo.GetListedData> searchBooks(@Parameter(description = "The book title") @RequestParam(required = false) String title,
                                                  @Parameter(description = "The book author") @RequestParam(required = false) String author) {
        List<BookTo.GetListedData> booksFound;

        LOGGER.info("Retrieving books to display for data " + title + "/" + author);
        List<Book> books = bookService.searchBooks(title, author);
        booksFound = books.stream().map(BookTo.GetListedData::new).toList();

        return booksFound;
    }

}
