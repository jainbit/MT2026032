package edu.esd.library;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The library catalogue: the books the library owns and how many copies of each.
 */
public class Catalogue {

    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Integer> totalCopies = new HashMap<>();
    private final Map<String, Integer> availableCopies = new HashMap<>();

    /** Adds a new book with the given number of copies. All copies start on the shelf. */
    public void addBook(Book book, int copies) {
        if (copies < 1) {
            throw new IllegalArgumentException("Copies must be at least 1");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }

        String isbn = book.getIsbn();
        if (books.containsKey(isbn)) {
            throw new DuplicateBookException(isbn);
        }

        books.put(isbn, book);
        totalCopies.put(isbn, copies);
        availableCopies.put(isbn, copies);
    }

    /** Returns a book by ISBN. */
    public Book findByIsbn(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    /** Finds books whose author contains the supplied text, ignoring case, sorted by title. */
    public List<Book> searchByAuthor(String name) {
        if (name == null) {
            return List.of();
        }

        String searchText = name.toLowerCase(Locale.ROOT);
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase(Locale.ROOT).contains(searchText))
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();
    }

    /** Lends one copy, if one is available. */
    public void lendCopy(String isbn) {
        findByIsbn(isbn);
        int available = availableCopies.get(isbn);
        if (available == 0) {
            throw new NoCopiesAvailableException(isbn);
        }
        availableCopies.put(isbn, available - 1);
    }

    /** Returns one borrowed copy to the shelf. */
    public void returnCopy(String isbn) {
        findByIsbn(isbn);
        int available = availableCopies.get(isbn);
        int total = totalCopies.get(isbn);
        if (available >= total) {
            throw new IllegalStateException("All copies are already on the shelf for ISBN " + isbn);
        }
        availableCopies.put(isbn, available + 1);
    }

    /** Returns the number of copies currently on the shelf. */
    public int availableCopies(String isbn) {
        findByIsbn(isbn);
        return availableCopies.get(isbn);
    }
}
