import java.util.Map;

public class HttpRequest {

    private HttpMethod method;
    private String path;
    private Map<String, String> headers;
    private String body;
    private String httpVersion;

    public HttpRequest(
        HttpMethod method,
        String path,
        Map<String, String> headers,
        String body,
        String httpVersion
    ) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getMethod() {
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

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String toString() {
        // return the request line
        // ex: GET /index.html HTTP/1.1
        // method, path, httpVersion
        return String.format("%s %s %s", method, path, httpVersion);
    }
}
