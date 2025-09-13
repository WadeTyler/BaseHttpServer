package playground.books;

import playground.books.handlers.GetBooksHandler;
import playground.books.handlers.PostBookHandler;
import server.request.HttpRequestHandler;
import server.route.Route;
import server.route.Routes;

import java.util.Map;

public class BookRoutes implements Routes {


    @Override
    public Map<Route, HttpRequestHandler> getRoutes() {
        return Map.of(
                new Route("/books", "GET"), new GetBooksHandler(),
                new Route("/books", "POST"), new PostBookHandler()
        );
    }
}