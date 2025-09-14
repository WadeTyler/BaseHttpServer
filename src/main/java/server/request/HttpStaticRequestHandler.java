package server.request;

import server.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A simple handler for serving static files.
 * It reads files from the classpath and returns their content with appropriate headers.
 * If the file is not found, it returns a 404 response.
 * Supported file types include HTML, CSS, JS, JSON, PNG, JPG, GIF, SVG, and PDF.
 * For unsupported file types, it defaults to "text/plain".
 */
public class HttpStaticRequestHandler {

    public HttpStaticRequestHandler() {
    }

    /**
     * Handles a request for a static file.
     * @param filePath the path to the static file (e.g., "/static/index.html")
     * @return an HttpResponse with the file content or a 404 if not found
     */
    public HttpResponse handleStaticFile(String filePath) {
        try {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            System.out.println("Serving static file: " + filePath);
            String contentType = getContentType(fileName);

            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                return new HttpResponse(404, "File Not Found")
                        .withContentType("text/plain");
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            is.close();

            return new HttpResponse(200, content)
                    .withContentType(contentType)
                    .withHeader("Content-Length", String.valueOf(content.length()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determines the content type based on the file extension.
     * @param fileName the name of the file
     * @return the corresponding content type
     */
    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "html", "htm" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "svg" -> "image/svg+xml";
            case "pdf" -> "application/pdf";
            default -> "text/plain";
        };
    }
}