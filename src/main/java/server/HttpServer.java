package server;

import server.request.HttpRequest;
import server.request.HttpRequestHandler;
import server.request.HttpRequestParser;
import server.response.HttpResponse;
import server.response.HttpResponseFormater;
import server.route.Route;
import server.route.Routes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple HTTP server that can handle multiple routes and concurrent requests.
 */
public class HttpServer implements Runnable {

    private final int port;
    private final ExecutorService threadPool;
    private final HashMap<Route, HttpRequestHandler> routes = new HashMap<>();

    public HttpServer(int port) {
        this.port = port;
        // Create a thread pool with a fixed number of threads
        this.threadPool = Executors.newFixedThreadPool(50);
    }


    /**
     * Add a route to the server
     * @param route the route to add
     * @param handler the handler for the route
     * @return the server instance (for chaining)
     */
    public HttpServer route(Route route, HttpRequestHandler handler) {
        this.routes.put(route, handler);
        return this;
    }

    public HttpServer routes(Routes routes) {
        this.routes.putAll(routes.getRoutes());
        return this;
    }

    // Convenience methods for common HTTP methods
    public HttpServer route(String path, HttpRequestHandler handler) {
        return route(new Route(path), handler);
    }

    public HttpServer post(String path, HttpRequestHandler handler) {
        return route(new Route(path, "POST"), handler);
    }

    public HttpServer get(String path, HttpRequestHandler handler) {
        return route(new Route(path, "GET"), handler);
    }

    public HttpServer put(String path, HttpRequestHandler handler) {
        return route(new Route(path, "PUT"), handler);
    }

    public HttpServer delete(String path, HttpRequestHandler handler) {
        return route(new Route(path, "DELETE"), handler);
    }

    public HttpServer patch(String path, HttpRequestHandler handler) {
        return route(new Route(path, "PATCH"), handler);
    }

    public HttpServer head(String path, HttpRequestHandler handler) {
        return route(new Route(path, "HEAD"), handler);
    }

    public HttpServer options(String path, HttpRequestHandler handler) {
        return route(new Route(path, "OPTIONS"), handler);
    }

    /**
     * Start the server
     * @throws IOException if the server fails to start
     */
    private void start() throws IOException {
        if (routes.isEmpty()) {
            System.out.println("Warning: No routes defined. Server will respond with 404 for all requests.");
        }
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("here");
            threadPool.submit(() -> handleClient(client));
        }
    }

    /**
     * Handle a client request
     * @param client the client socket
     */
    private void handleClient(Socket client) {
        try {
            // Parse into HttpRequest object
            HttpRequest request = HttpRequestParser.parse(client);
            System.out.println("Received request: " + request.getMethod() + " " + request.getPath());

            HttpResponse response = new HttpResponse(404, "Not Found"); // If no handler found, will be 404

            // Find handler for route
            Route requestRoute = new Route(request.getPath(), request.getMethod());
            if (routes.containsKey(requestRoute)) {
                HttpRequestHandler handler = routes.get(requestRoute);
                response = handler.handle(request);
            }

            // Format HttpResponse object into string
            String httpResponseStr = HttpResponseFormater.format(response);

            // Write response back to client
            client.getOutputStream().write(httpResponseStr.getBytes());
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse response = new HttpResponse(500, "Internal Server Error");
            try {
                String httpResponseStr = HttpResponseFormater.format(response);
                client.getOutputStream().write(httpResponseStr.getBytes());
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Run the server (for Runnable interface)
     */
    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start the server in a new thread
     * @return the thread the server is running in
     */
    public Thread startServer() {
        Thread serverThread = new Thread(this);
        serverThread.start();
        return serverThread;
    }
}