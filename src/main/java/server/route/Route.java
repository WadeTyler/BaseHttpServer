package server.route;

import java.util.Objects;

public class Route {
    private final String path;
    private final String method;

    public Route(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public Route(String path) {
        this.path = path;
        this.method = "GET";
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(path, route.path) && Objects.equals(method, route.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}