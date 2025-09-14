package playground.books.handlers;

import playground.books.Book;
import playground.books.BookDao;
import playground.books.handlers.dto.CreateBookRequest;
import server.request.HttpRequest;
import server.request.HttpRequestHandler;
import server.response.HttpResponse;

public class PostBookHandler implements HttpRequestHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        CreateBookRequest createBookRequest = request.getBodyAs(CreateBookRequest.class);

        if (createBookRequest == null) {
            return new HttpResponse()
                    .withStatus(400)
                    .withBody("Invalid request body.");
        }

        if (createBookRequest.title() == null || createBookRequest.title().isBlank()) {
            return new HttpResponse()
                    .withStatus(400)
                    .withBody("Tile is required.");
        }

        Book book = new Book(null, createBookRequest.title());
        Book savedBook = BookDao.addBook(book);

        return new HttpResponse().withStatus(201).withHeader("Content-Type", "application/json")
                .withBody(savedBook);
    }
}