package server.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private static final boolean DEBUG = true;

    public static HttpRequest parse(Socket client) throws IOException {
        InputStream is = client.getInputStream();

        // Read request-line, skipping any leading empty lines per RFC robustness
        String requestLine = readLine(is);
        while (requestLine != null && requestLine.isEmpty()) {
            requestLine = readLine(is);
        }
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request");
        }

        if (DEBUG) {
            System.out.println("Request-Line: " + requestLine);
        }

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

        // Parse headers until blank line
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = readLine(is)) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        // Parse body (only if Content-Length specified)
        String body = null;
        if (headers.containsKey("Content-Length")) {
            int contentLength = 0;
            try {
                contentLength = Integer.parseInt(headers.get("Content-Length"));
            } catch (NumberFormatException ignored) {
                contentLength = 0;
            }
            if (contentLength > 0) {
                byte[] bodyBytes = readFixedLength(is, contentLength);
                body = new String(bodyBytes, StandardCharsets.UTF_8);
            } else {
                body = "";
            }
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

    // Read a single line terminated by CRLF or LF; returns line without terminator.
    private static String readLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int prev = -1;
        int b;
        while ((b = is.read()) != -1) {
            if (b == '\n') { // LF
                break; // End of line (CR may have been before)
            }
            if (prev == '\r' && b != '\n') {
                // Lone CR encountered; treat previous CR as line end and continue processing this byte in next call
                // Push back is not available; so we append current char and will return previous line.
            }
            sb.append((char) b);
            prev = b;
            if (prev == '\r') {
                // Peek next byte to see if it's \n; if not, we'll consider CR as line end on next iteration.
            }
            // Safety: limit very long lines
            if (sb.length() > 8192) {
                break;
            }
        }
        // Trim trailing CR if present
        int len = sb.length();
        if (len > 0 && sb.charAt(len - 1) == '\r') {
            sb.setLength(len - 1);
        }
        return sb.toString();
    }

    // Read exactly n bytes from InputStream
    private static byte[] readFixedLength(InputStream is, int length) throws IOException {
        byte[] data = new byte[length];
        int offset = 0;
        while (offset < length) {
            int read = is.read(data, offset, length - offset);
            if (read == -1) {
                break;
            }
            offset += read;
        }
        if (offset < length) {
            byte[] partial = new byte[offset];
            System.arraycopy(data, 0, partial, 0, offset);
            return partial;
        }
        return data;
    }
}