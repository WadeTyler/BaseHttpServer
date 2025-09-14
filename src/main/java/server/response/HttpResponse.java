package server.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private Object body;
    private String httpVersion = "HTTP/1.1";


    public HttpResponse(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;

        // Set default headers if not provided
        this.headers.putIfAbsent("Content-Length", body != null ? String.valueOf(getBodyAsString().length()) : "0");
        this.headers.putIfAbsent("Connection", "close");
        this.headers.putIfAbsent("Content-Type", "text/plain");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

    public String getBodyAsString() {
        if (body instanceof String) {
            return (String) body;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            return body != null ? body.toString() : null;
        }
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void putHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}