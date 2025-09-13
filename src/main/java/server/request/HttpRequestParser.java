package server.request;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private static final boolean DEBUG = false;

    public static HttpRequest parse(Socket client) throws IOException {
        // Read all bytes and convert to input string
        int availableBytes = client.getInputStream().available();
        String inputString = new String(client.getInputStream().readNBytes(availableBytes));
        // Split into lines
        String[] lines = inputString.split("\r\n");
        debugLines(lines);

        if (lines.length == 0) {
            throw new IOException("Empty request");
        }

        // Extract method from request line
        String requestLine = lines[0];
        // Parse method, path, and HTTP version from request line
        String[] requestLineParts = requestLine.split(" ", 3);
        if (requestLineParts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];
        Map<String, String> queryParams = parseQueryParams(path);
        // Remove query parameters from path
        path = path.split("\\?")[0];

        // Parse headers
        Map<String, String> headers = new HashMap<>();
        int lineIndex = 1;
        while (lineIndex < lines.length && !lines[lineIndex].isEmpty()) {
            String headerLine = lines[lineIndex];
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
            lineIndex++;
        }

        // Parse body
        String body = null;
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            StringBuilder bodyBuilder = new StringBuilder();
            lineIndex++; // Move to the line after headers
            while (lineIndex < lines.length) {
                bodyBuilder.append(lines[lineIndex]);
                if (bodyBuilder.length() >= contentLength) {
                    break;
                }
                bodyBuilder.append("\r\n");
                lineIndex++;
            }
            body = bodyBuilder.toString();
        }

        return new HttpRequest(httpVersion, method, path, headers, body, queryParams);
    }

    private static Map<String, String> parseQueryParams(String path) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pathParts = path.split("\\?", 2);
        if (pathParts.length == 2) {
            // Split into pairs "key=value"
            String queryString = pathParts[1];
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                // Parse each pair
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }


    private static void debugLines(String[] lines) {
        if (DEBUG) {
            System.out.println("---- DEBUG: Request Lines ----");
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("---- END DEBUG ----");
        }
    }
}