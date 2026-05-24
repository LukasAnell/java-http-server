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
        //

        return null;
    }

    public boolean hasRoute(HttpMethod method, String path) {
        //

        return false;
    }
}
