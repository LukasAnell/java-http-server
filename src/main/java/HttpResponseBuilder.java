import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {

    private HttpStatus status;
    private Map<String, String> headers;
    private String body;

    public HttpResponseBuilder() {
        this.status = HttpStatus.OK;
        this.headers = new HashMap<>();
        this.body = "";
    }

    public HttpResponseBuilder setStatus(HttpStatus status) {
        this.status = status;

        return this;
    }

    public HttpResponseBuilder addHeader(String name, String value) {
        headers.put(name, value);

        return this;
    }

    public HttpResponseBuilder setBody(String body) {
        this.body = body;

        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(status, new HashMap<>(headers), body);
    }
}
