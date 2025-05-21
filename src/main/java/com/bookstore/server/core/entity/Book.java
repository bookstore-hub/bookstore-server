package com.bookstore.server.core.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "book", schema="bookstore")
public class Book extends DomainObject {

    @Id
    @GeneratedValue(generator = "book_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "book_sequence", sequenceName = "bookstore.book_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "date_of_publication")
    private LocalDate dateOfPublication;

    @Column(name = "synopsis", length = 300)
    private String synopsis;

    @Column(name = "number_of_pages")
    private int numberOfPages;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();


    /**
     * Default constructor
     */
    public Book() {
    }

    /**
     * Constructor with parameters
     *
     * @param code    The code of the book
     * @param title   The title of the book
     * @param authors The authors of the book
     */
    public Book(String code, String title, Set<Author> authors) {
        this.code = code;
        this.title = title;
        this.authors = authors;
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
     * Returns the title
     *
     * @return Returns the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the dateOfPublication
     *
     * @return Returns the dateOfPublication
     */
    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    /**
     * Sets the dateOfPublication
     *
     * @param dateOfPublication The dateOfPublication to set
     */
    public void setDateOfPublication(LocalDate dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    /**
     * Returns the synopsis
     *
     * @return Returns the synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Sets the synopsis
     *
     * @param synopsis The synopsis to set
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Returns the numberOfPages
     *
     * @return Returns the numberOfPages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the numberOfPages
     *
     * @param numberOfPages The numberOfPages to set
     */
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * Returns the authors
     *
     * @return Returns the authors
     */
    public Set<Author> getAuthors() {
        return authors;
    }

    /**
     * Sets the authors
     *
     * @param authors The authors to set
     */
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        getAuthors().add(author);
        author.addBook(this);
    }

    public void removeAuthor(Author author) {
        getAuthors().remove(author);
        author.removeBook(this);
    }

    public boolean hasOnlyOneAuthor() {
        return getAuthors().size() == 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(code, book.code);
    }

}
