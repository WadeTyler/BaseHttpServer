package playground.books;

import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public static List<Book> books = new ArrayList<>();

    public static Book getById(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Book addBook(Book book) {
        // Set bookId
        if (books.isEmpty()) {
            book.setId("1");
        } else {
            String lastId = books.getLast().getId();
            int newId = Integer.parseInt(lastId) + 1;
            book.setId(String.valueOf(newId));
        }

        books.add(book);
        return book;
    }
}