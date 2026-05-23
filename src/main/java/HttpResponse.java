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
        StringBuilder output = new StringBuilder("HTTP/1.1 ");

        // response header line should be of the format:
        // httpVersion code responseStr
        output.append(status.getCode()).append(" ").append(status.getReason());

        // append newline to end of each line
        output.append("\r\n");

        // append headers to output in any order
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            // get key and value separately
            String key = entry.getKey();
            String value = entry.getValue();

            output.append(key).append(": ").append(value);
            output.append("\r\n");
        }

        // append newline regardless of whether or not there is a body
        output.append("\r\n");

        output.append(getBody());

        return output.toString();
    }
}
