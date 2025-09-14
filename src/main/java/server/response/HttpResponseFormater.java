package server.response;

/**
 * Formats an HttpResponse into a raw HTTP response string.
 */
public class HttpResponseFormater {

    // format only the status line and headers (ending with CRLF CRLF)

    /**
     * Formats the status line and headers of an HttpResponse into a raw HTTP response string.
     * @param response the HttpResponse to format
     * @return the formatted HTTP response string
     */
    public static String formatHeaders(HttpResponse response) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(response.getHttpVersion())
                .append(" ")
                .append(response.getStatusCode())
                .append(" ")
                .append(getReasonPhrase(response.getStatusCode()))
                .append("\r\n");

        if (response.getHeaders() != null) {
            for (var header : response.getHeaders().entrySet()) {
                responseBuilder.append(header.getKey())
                        .append(": ")
                        .append(header.getValue())
                        .append("\r\n");
            }
        }
        responseBuilder.append("\r\n");
        return responseBuilder.toString();
    }

    /**
     * Gets the standard reason phrase for a given HTTP status code.
     * @param statusCode the HTTP status code
     * @return the corresponding reason phrase
     */
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