package server.request;

import server.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HttpStaticRequestHandler {

    public HttpStaticRequestHandler() {
    }

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