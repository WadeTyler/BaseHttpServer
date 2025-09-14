import playground.books.BookRoutes;
import server.HttpServer;

public class Application {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server
                .routes(new BookRoutes())
                .staticFiles("/hidden", "hidden")
                .staticFiles("/", "/static");
        server.startServer();
    }
}