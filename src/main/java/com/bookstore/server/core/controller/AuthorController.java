
package com.bookstore.server.core.controller;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.exception.PreconditionFailedException;
import com.bookstore.server.core.repository.AuthorRepository;
import com.bookstore.server.core.repository.BookRepository;
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

import java.util.Optional;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/author")
@Tag(name = "AuthorController", description = "Set of endpoints to handle the Bookstore author logic")
public class AuthorController {

    protected static final Log LOGGER = LogFactory.getLog(AuthorController.class);

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    /**
     * Creates a new author
     *
     * @param authorName    the author name
     */
    @Operation(description = "Creates a new author")
    @PostMapping
    public AuthorTo.GetData createAuthor(@Parameter(description = "The author data") @RequestParam String authorName) {
        Optional<Author> existingAuthor = authorRepository.findByName(authorName);
        if(existingAuthor.isPresent()) {
            throw new EntityExistsException("The author " + authorName + " is already present in the database.");
        }

        LOGGER.info("Creating author " + authorName);

        Author newAuthor = AuthorTo.mapNewAuthor(authorName);
        authorRepository.save(newAuthor);

        return new AuthorTo.GetData(newAuthor);
    }

    /**
     * Edits an author entry
     *
     * @param authorCode     the author code
     */
    @Operation(description = "Edits an author entry")
    @PutMapping
    public AuthorTo.GetData editAuthor(@Parameter(description = "The author code") @RequestParam String authorCode,
                                       @Parameter(description = "The author name") @RequestParam String authorName) {

        Optional<Author> authorToEdit = authorRepository.findByCode(authorCode);
        if(authorToEdit.isPresent()) {
            Author author = authorToEdit.get();

            LOGGER.info("Editing author entry for " + author.getName());
            author.setName(authorName);
            authorRepository.save(author);

            return new AuthorTo.GetData(author);
        } else {
            throw new EntityNotFoundException("The author to edit with code " + authorCode + " could not be found.");
        }

    }

    /**
     * Removes an author
     *
     * @param authorCode    the author code
     */
    @Operation(description = "Removes an author")
    @DeleteMapping
    public void removeAuthor(@Parameter(description = "The author code") @RequestParam String authorCode) {
        Optional<Author> authorToRemove = authorRepository.findByCode(authorCode);
        if(authorToRemove.isPresent()) {
            Author author = authorToRemove.get();
            if(author.hasBookWithOneAuthorOnly()) {
                throw new PreconditionFailedException("The author can't be deleted because each book must have at least one author.");
            }

            LOGGER.info("Removing author " + authorToRemove.get().getName());
            authorRepository.delete(authorToRemove.get());
        } else {
            throw new EntityNotFoundException("The author to delete with code " + authorCode + " could not be found.");
        }
    }

    /**
     * Adds an author to a book
     *
     * @param authorCode  the author name
     * @param bookCode    the book code
     */
    @Operation(description = "Adds an author to a book")
    @PutMapping("/addToBook")
    @ResponseStatus
    public BookTo.GetData addAuthorToBook(@Parameter(description = "The author code") @RequestParam String authorCode,
                                          @Parameter(description = "The book code") @RequestParam String bookCode) {
        Optional<Author> authorToAdd = authorRepository.findByCode(authorCode);
        Optional<Book> bookToAdd = bookRepository.findByCode(bookCode);

        if(authorToAdd.isPresent() && bookToAdd.isPresent()) {
            Author author = authorToAdd.get();
            Book book = bookToAdd.get();

            LOGGER.info("Adding author " + author.getName() + " to book " + book.getTitle());
            book.addAuthor(authorToAdd.get());
            bookRepository.save(book);

            return new BookTo.GetData(book);
        } else {
            throw new EntityNotFoundException("Author could not be added to book. Either the author " + authorCode +
                    " or the book " + bookCode + " could not be found.");
        }
    }

    /**
     * Removes an author from a book
     *
     * @param authorCode  the author name
     * @param bookCode    the book code
     */
    @Operation(description = "Removes an author to a book")
    @PutMapping("/removeFromBook")
    @ResponseStatus
    public BookTo.GetData removeAuthorFromBook(@Parameter(description = "The author code") @RequestParam String authorCode,
                                               @Parameter(description = "The book code") @RequestParam String bookCode) {
        Optional<Author> authorToRemove = authorRepository.findByCode(authorCode);
        Optional<Book> bookToRemove = bookRepository.findByCode(bookCode);

        if(authorToRemove.isPresent() && bookToRemove.isPresent()) {
            Author author = authorToRemove.get();
            Book book = bookToRemove.get();

            if(book.hasOnlyOneAuthor()) {
                throw new PreconditionFailedException("The author can't be deleted because the book with code " + bookCode + " only has one author.");
            }

            LOGGER.info("Removing author " + author.getName() + " from book " + book.getTitle());
            book.removeAuthor(authorToRemove.get());
            bookRepository.save(book);

            return new BookTo.GetData(book);
        } else {
            throw new EntityNotFoundException("Author could not be removed from book. Either the author " + authorCode +
                    " or the book " + bookCode + " could not be found.");
        }
    }

}
