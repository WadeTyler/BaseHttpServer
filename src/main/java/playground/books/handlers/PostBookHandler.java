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

        if (createBookRequest.title() == null || createBookRequest.title().isBlank()) {
            return new HttpResponse(400, "Title is required");
        }

        Book book = new Book(null, createBookRequest.title());
        Book savedBook = BookDao.addBook(book);

        var response = new HttpResponse(201, savedBook);
        response.putHeader("Content-Type", "application/json");
        return response;
    }
}