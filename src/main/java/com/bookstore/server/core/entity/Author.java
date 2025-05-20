package com.bookstore.server.core.entity;


import jakarta.persistence.*;

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
    private List<Book> books;


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
        return books;
    }

    /**
     * Sets the books
     *
     * @param books The books to set
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) { //todo check
        getBooks().add(book);
    }

    public void removeBook(Book book) { //todo check
        getBooks().remove(book);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(code, author.code);
    }

}
