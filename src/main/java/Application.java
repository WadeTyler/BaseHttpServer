import playground.books.BookRoutes;
import server.HttpServer;

public class Application {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server
                .rateLimit(5)
                .routes(new BookRoutes())
                .staticFiles("/hidden", "hidden")
                .staticFiles("/", "/static");
        server.startServer();
    }
}