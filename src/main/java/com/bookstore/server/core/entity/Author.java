package com.bookstore.server.core.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "author", schema="bookstore")
public class Author extends DomainObject {

    @Id
    @GeneratedValue(generator = "author_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "author_sequence", sequenceName = "bookstore.author_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "name", length = 30)
    private String name;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();


    /**
     * Default constructor
     */
    public Author() {
    }

    /**
     * Constructor with name
     *
     * @param name The name of the author
     */
    public Author(String name) {
        this.name = name;
    }

    /**
     * Constructor with code and name
     *
     * @param code The code of the author
     * @param name The name of the author
     */
    public Author(String code, String name) {
        this.code = code;
        this.name = name;
    }


    /**
     * Returns the id
     *
     * @return Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the code
     *
     * @return Returns the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     *
     * @param code The code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the name
     *
     * @return Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the books
     *
     * @return Returns the books
     */
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Sets the books
     *
     * @param books The books to set
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        getBooks().add(book);
    }

    public void removeBook(Book book) {
        getBooks().remove(book);
    }

    public boolean hasBookWithOneAuthorOnly() {
        return getBooks().stream().anyMatch(book -> book.getAuthors().size() == 1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(code, author.code);
    }

}
