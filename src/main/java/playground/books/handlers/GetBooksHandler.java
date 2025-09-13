package playground.books.handlers;

import playground.books.Book;
import playground.books.BookDao;
import server.request.HttpRequest;
import server.request.HttpRequestHandler;
import server.response.HttpResponse;

import java.util.List;

public class GetBooksHandler implements HttpRequestHandler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        // Check for id
        if (request.getQueryParams().containsKey("id")) {
            String id = request.getQueryParams().get("id");
            Book book = BookDao.getById(id);
            if (book == null) {
                return new HttpResponse(404, "Book not found");
            }
            var response = new HttpResponse(200, book);
            response.putHeader("Content-Type", "application/json");
            return response;
        }

        List<Book> books = BookDao.books;
        var response = new HttpResponse(200, books);
        response.putHeader("Content-Type", "application/json");
        return response;
    }
}