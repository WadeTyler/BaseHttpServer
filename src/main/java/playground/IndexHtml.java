package playground;

import server.request.HttpRequest;
import server.request.HttpRequestHandler;
import server.response.HttpResponse;

public class IndexHtml implements HttpRequestHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        String content = """
                <h1>Welcome to the Home Page</h1>
                """;

        return new HttpResponse(200, content)
                .withContentType("text/html");
    }
}