package server.response;

import com.fasterxml.jackson.core.JsonProcessingException;

public class HttpResponseFormater {
    public static String format(HttpResponse response) throws JsonProcessingException {
        StringBuilder responseBuilder = new StringBuilder();

        // Start with the status line
        responseBuilder.append(response.getHttpVersion())
                .append(" ")
                .append(response.getStatusCode())
                .append(" ")
                .append(getReasonPhrase(response.getStatusCode()))
                .append("\r\n");

        // Add headers
        if (response.getHeaders() != null) {
            for (var header : response.getHeaders().entrySet()) {
                responseBuilder.append(header.getKey())
                        .append(": ")
                        .append(header.getValue())
                        .append("\r\n");
            }
        }
        responseBuilder.append("\r\n");

        // Add body if present
        if (response.getBody() != null) {
            responseBuilder.append(response.getBodyAsString());
        }

        return responseBuilder.toString();
    }

    private static String getReasonPhrase(int statusCode) {
        return switch (statusCode) {
            // 2xx Success
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";

            // 3xx Redirection
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 304 -> "Not Modified";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";

            // 4xx Client Error
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 429 -> "Too Many Requests";

            // 5xx Server Error
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            default -> "Unknown Status";
        };
    }
}