package com.bookstore.server.core.to;

import com.bookstore.server.core.entity.Author;
import com.bookstore.server.core.util.RandomCodeGenerator;

import java.time.LocalDateTime;


public class AuthorTo {


    /** Retrieval of Author **/
    public record GetData(String code, String name) {
        public GetData(Author author) {
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
