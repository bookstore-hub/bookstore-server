package com.bookstore.server.core.to;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.util.RandomCodeGenerator;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


public class AuthorTo {


    /** Create Author **/
    public record NewAuthorData(
            @Parameter(description = "The author name")
            @Size(max = 30)
            @NotNull
            String authorName
    ) {}

    /** Retrieval of Author **/
    public record GetAuthorData(String authorCode, String authorName) {
        public GetAuthorData(Author author) {
            this(author.getCode(), author.getName());
        }
    }

    /** Mapping of new Author **/
    public static Author mapNewAuthor(String authorName) {
        Author author = new Author();
        author.setCode(RandomCodeGenerator.generateAlphaNumericCode());
        author.setName(authorName);
        author.setLastModificationDate(LocalDateTime.now());

        return author;
    }

}
