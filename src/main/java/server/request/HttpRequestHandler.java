package server.request;

import server.response.HttpResponse;

public interface HttpRequestHandler {
    HttpResponse handle(HttpRequest request);
}