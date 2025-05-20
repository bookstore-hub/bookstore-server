package com.bookstore.server.core.to;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.entity.Book;
import com.bookstore.server.core.util.DateUtil;
import com.bookstore.server.core.util.RandomCodeGenerator;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class BookTo {

    /**
     * Note :
     * Cette approche basée sur l'utilisation des records est intéressante dans ce cas précis en raison du nombre limité de champs.
     * Cela nous permet d'implémenter toute la logique DTO dans une seule classe.
     * Pour une quantité substantielle de champs, nous donnerions priorité à l'utilisation de lombok.
     */

    /** Create or Update Book **/
    public record NewData(
        @Parameter (description = "The title")
        @Size(max = 30)
        @NotNull
        String title,
        @Parameter (description = "The date of publication")
        LocalDate dateOfPublication,
        @Parameter (description = "The synopsis")
        @Size(max = 300)
        String synopsis,
        @Parameter (description = "The number of pages")
        @NotNull
        int numberOfPages,
        @Parameter (description = "The author(s)")
        @NotNull
        List<String> authors
    ) {}


    /** Retrieval of Book **/
    public record GetData(String code, String title, String dateOfPublication, String synopsis, int numberOfPages, String authors) {
        public GetData(Book book) {
            this(book.getCode(), book.getTitle(), resolveDate(book.getDateOfPublication()), book.getSynopsis(), book.getNumberOfPages(), mapAuthors(book.getAuthors()));
        }
    }

    /** Retrieval of Book for listing **/
    public record GetListedData(String code, String title, String authors) {
        public GetListedData(Book book) {
            this(book.getCode(), book.getTitle(), mapAuthors(book.getAuthors()));
        }
    }

    private static String resolveDate(LocalDate dateOfPublication) {
        return DateUtil.formatDate(dateOfPublication);
    }

    private static String mapAuthors(List<Author> authors) {
        return authors.stream().map(Author::getName).collect(Collectors.joining(","));
    }


    /** Mapping of new Book **/
    public static Book mapNewBook(NewData bookData) {
        Book book = new Book();
        book.setCode(RandomCodeGenerator.generateAlphaNumericCode());
        updateBook(book, bookData);

        List<Author> authors = bookData.authors().stream().map(AuthorTo::mapNewAuthor).toList();
        authors.forEach(book::addAuthor);

        return book;
    }

    /** Mapping of updated Book **/
    public static void mapUpdatedBook(Book book, NewData bookData) {
        updateBook(book, bookData);
    }

    public static void updateBook(Book book, NewData bookData) {
        book.setTitle(bookData.title());
        book.setDateOfPublication(bookData.dateOfPublication());
        book.setSynopsis(bookData.synopsis());
        book.setNumberOfPages(bookData.numberOfPages());
        book.setLastModificationDate(LocalDateTime.now());
    }

}
