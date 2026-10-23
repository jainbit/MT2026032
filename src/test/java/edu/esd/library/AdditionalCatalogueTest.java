package edu.esd.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdditionalCatalogueTest {

    @Test
    void rejectsNonDigitIsbnAndFutureYear() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book("97801346859A1", "Example", "Author", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("9780134685991", "Example", "Author", java.time.Year.now().getValue() + 1));
    }

    @Test
    void rejectsInvalidCopyCount() {
        Catalogue catalogue = new Catalogue();
        Book book = new Book("9780134685991", "Example", "Author", 2020);
        assertThrows(IllegalArgumentException.class, () -> catalogue.addBook(book, 0));
    }

    @Test
    void cannotReturnCopyWhenNoneWasBorrowed() {
        Catalogue catalogue = new Catalogue();
        Book book = new Book("9780134685991", "Example", "Author", 2020);
        catalogue.addBook(book, 1);
        assertThrows(IllegalStateException.class, () -> catalogue.returnCopy(book.getIsbn()));
        assertEquals(1, catalogue.availableCopies(book.getIsbn()));
    }

    @Test
    void missingBookThrowsException() {
        Catalogue catalogue = new Catalogue();
        assertThrows(BookNotFoundException.class, () -> catalogue.findByIsbn("9780134685991"));
        assertThrows(BookNotFoundException.class, () -> catalogue.availableCopies("9780134685991"));
    }
}
