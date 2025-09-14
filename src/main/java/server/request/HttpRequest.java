package server.request;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class HttpRequest {
    private final String httpVersion;
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String httpVersion, String method, String path, Map<String, String> headers, String body, Map<String, String> queryParams) {
        this.httpVersion = httpVersion;
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    /**
     * Convert the body to the specified class using Jackson ObjectMapper.
     * @param clazz the class to convert the body to
     * @return the body as the specified class, or null if conversion fails
     * @param <T> the type of the class
     */
    public <T> T getBodyAs(Class<T> clazz) {
        // Use Jackson ObjectMapper to convert body to specified class
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpVersion='" + httpVersion + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParams=" + queryParams +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}