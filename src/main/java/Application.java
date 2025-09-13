import playground.books.BookRoutes;
import server.HttpServer;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();
        server.routes(new BookRoutes());
        server.start(8080);
    }
}