import java.util.Collections;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(
        HttpStatus status,
        Map<String, String> headers,
        String body
    ) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getBody() {
        return body;
    }

    public String serialize() {
        // convert response to valid String

        return null;
    }
}
