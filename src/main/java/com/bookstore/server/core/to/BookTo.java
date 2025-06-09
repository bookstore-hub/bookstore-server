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
import java.util.Set;


public class BookTo {

    /* Note:
     * Cette approche basée sur l'utilisation des records est intéressante dans ce cas précis en raison du nombre limité de champs.
     * Cela nous permet d'implémenter toute la logique DTO dans une seule classe.
     * Pour une quantité substantielle de champs, nous donnerions priorité à l'utilisation de lombok.
     */

    /** Create or Update Book **/
    public record NewBookData(
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
    public record GetBookData(String bookCode, String title, String dateOfPublication, String synopsis, int numberOfPages, List<AuthorTo.GetAuthorData> authors) {
        public GetBookData(Book book) {
            this(book.getCode(), book.getTitle(), resolveDate(book.getDateOfPublication()), book.getSynopsis(), book.getNumberOfPages(), mapAuthors(book.getAuthors()));
        }
    }

    /** Retrieval of Book for listing **/
    public record GetBookListedData(String bookCode, String title, List<AuthorTo.GetAuthorData> authors) {
        public GetBookListedData(Book book) {
            this(book.getCode(), book.getTitle(), mapAuthors(book.getAuthors()));
        }
    }

    private static String resolveDate(LocalDate dateOfPublication) {
        return DateUtil.formatDate(dateOfPublication);
    }

    private static List<AuthorTo.GetAuthorData> mapAuthors(Set<Author> authors) {
        return authors.stream().map(AuthorTo.GetAuthorData::new).toList();
    }


    /** Mapping of new Book **/
    public static Book mapNewBook(NewBookData bookData) {
        Book book = new Book();
        book.setCode(RandomCodeGenerator.generateAlphaNumericCode());
        updateBook(book, bookData);

        return book;
    }

    /** Mapping of updated Book **/
    public static void mapUpdatedBook(Book book, NewBookData bookData) {
        updateBook(book, bookData);
    }

    public static void updateBook(Book book, NewBookData bookData) {

        if(bookData.title() != null) book.setTitle(bookData.title());
        if(bookData.dateOfPublication() != null) book.setDateOfPublication(bookData.dateOfPublication());
        if(bookData.synopsis() != null) book.setSynopsis(bookData.synopsis());
        if(bookData.numberOfPages() != 0) book.setNumberOfPages(bookData.numberOfPages());

        book.setLastModificationDate(LocalDateTime.now());

    }

}
