import java.util.HashMap;

public class Router {

    // OUTER_KEY: (INNER_KEY: VALUE)
    // filePath: HttpMethod, Handler
    private HashMap<String, HashMap<HttpMethod, Handler>> routingTable;

    public Router() {
        routingTable = new HashMap<>();
    }

    public void register(HttpMethod method, String path, Handler handler) {
        /* equivalent to:
         * routingTable.putIfAbsent(path, new HashMap<>());
         * routingTable.get(path).put(method, handler);
         */
        routingTable
            .computeIfAbsent(path, k -> new HashMap<>())
            .put(method, handler);
    }

    public HttpResponse dispatch(HttpRequest request) {
        // get requested path
        String path = request.getPath();

        // get HttpMethod
        HttpMethod method = request.getMethod();

        if (!routingTable.containsKey(path)) {
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.NOT_FOUND)
                .addHeader("Content-Type", "text/plain")
                .setBody("404 Not Found")
                .build();
        }

        if (!routingTable.get(path).containsKey(method)) {
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .addHeader("Content-Type", "text/plain")
                .setBody("405 Method Not Allowed")
                .build();
        }

        // create lambda Handler
        // calls Handler.handle() on a given HttpRequest
        Handler handler = r -> routingTable.get(path).get(method).handle(r);

        // return result of handled request
        return handler.handle(request);
    }

    public boolean hasRoute(HttpMethod method, String path) {
        if (!routingTable.containsKey(path)) {
            return false;
        }

        if (!routingTable.get(path).containsKey(method)) {
            return false;
        }

        return true;
    }
}
