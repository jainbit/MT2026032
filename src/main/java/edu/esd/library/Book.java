package edu.esd.library;

import java.time.Year;
import java.util.Objects;

/**
 * A book in the library catalogue.
 *
 * <p>Guidelines:
 * <ul>
 *   <li>Fields: isbn, title, author, year -- all {@code private final}. No setters.</li>
 *   <li>Two books are equal if and only if they have the same ISBN.</li>
 * </ul>
 */
public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final int year;

    /**
     * Creates a book.
     *
     * @throws IllegalArgumentException if the ISBN, title, author, or year is invalid
     */
    public Book(String isbn, String title, String author, int year) {
        if (isbn == null || !isbn.matches("[0-9]{13}")) {
            throw new IllegalArgumentException("ISBN must contain exactly 13 digits");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author must not be blank");
        }
        if (year > Year.now().getValue()) {
            throw new IllegalArgumentException("Publication year cannot be in the future");
        }

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Book other)) {
            return false;
        }
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', title='" + title + "'}";
    }
}
