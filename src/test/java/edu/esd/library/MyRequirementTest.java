package edu.esd.library;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Extra checks for assignment rules not covered by the supplied examples. */
class MyRequirementTest {

    @Test
    void rejectsEveryInvalidBookInputCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book(null, "Title", "Author", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("12345678901234", "Title", "Author", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("1234567890123", null, "Author", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("1234567890123", "Title", "\t ", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("1234567890123", "Title", "Author", Year.now().getValue() + 1));
    }

    @Test
    void searchReturnsEmptyListAndMatchesPartialAuthorText() {
        Catalogue catalogue = new Catalogue();
        Book alpha = new Book("1234567890123", "Alpha", "Ada Lovelace", 1843);
        Book beta = new Book("1234567890124", "Beta", "Grace Hopper", 1952);
        catalogue.addBook(beta, 1);
        catalogue.addBook(alpha, 1);

        assertEquals(List.of(alpha), catalogue.searchByAuthor("LOVE"));
        assertTrue(catalogue.searchByAuthor("unknown").isEmpty());
    }

    @Test
    void lendingAndReturningMaintainTheAvailableCountAndRespectLimits() {
        Catalogue catalogue = new Catalogue();
        Book book = new Book("1234567890123", "Title", "Author", 2020);
        catalogue.addBook(book, 2);

        catalogue.lendCopy(book.getIsbn());
        catalogue.lendCopy(book.getIsbn());
        assertEquals(0, catalogue.availableCopies(book.getIsbn()));
        assertThrows(NoCopiesAvailableException.class, () -> catalogue.lendCopy(book.getIsbn()));

        catalogue.returnCopy(book.getIsbn());
        assertEquals(1, catalogue.availableCopies(book.getIsbn()));
        catalogue.returnCopy(book.getIsbn());
        assertThrows(IllegalStateException.class, () -> catalogue.returnCopy(book.getIsbn()));
    }

    @Test
    void allIsbnOperationsRejectAMissingBook() {
        Catalogue catalogue = new Catalogue();
        String missing = "1234567890123";

        assertThrows(BookNotFoundException.class, () -> catalogue.findByIsbn(missing));
        assertThrows(BookNotFoundException.class, () -> catalogue.availableCopies(missing));
        assertThrows(BookNotFoundException.class, () -> catalogue.lendCopy(missing));
        assertThrows(BookNotFoundException.class, () -> catalogue.returnCopy(missing));
    }

    @Test
    void bookIdentityUsesOnlyItsIsbnAndStringShowsTheKeyDetails() {
        Book first = new Book("1234567890123", "One", "Author A", 2000);
        Book sameIsbn = new Book("1234567890123", "Two", "Author B", 2001);
        Book differentIsbn = new Book("1234567890124", "One", "Author A", 2000);

        assertEquals(first, sameIsbn);
        assertEquals(first.hashCode(), sameIsbn.hashCode());
        assertFalse(first.equals(differentIsbn));
        assertTrue(first.toString().contains(first.getIsbn()));
        assertTrue(first.toString().contains(first.getTitle()));
    }
}
