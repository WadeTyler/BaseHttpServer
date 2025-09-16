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
                return new HttpResponse()
                        .withStatus(404)
                        .withBody("Book not found");
            }
            var response = new HttpResponse(200, book);
            response.putHeader("Content-Type", "application/json");
            return new HttpResponse()
                    .withStatus(200)
                    .withContentType("application/json")
                    .withBody(book);
        }

        List<Book> books = BookDao.books;
        return new HttpResponse()
                .withStatus(200)
                .json(books);
    }
}