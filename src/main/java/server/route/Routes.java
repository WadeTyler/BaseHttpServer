package server.route;

import server.request.HttpRequestHandler;

import java.util.Map;

public interface Routes {

    Map<Route, HttpRequestHandler> getRoutes();
}