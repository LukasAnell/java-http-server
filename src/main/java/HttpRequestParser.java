import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public static HttpRequest parse(String raw)
        throws IllegalArgumentException {
        /* Request format example:
         * GET /index.html HTTP/1.1\r\n     # request line (METHOD, PATH, HTTP_VERSION)
         * Host: localhost\r\n              # header
         * Content-Type: text/html\r\n      # header
         * \r\n                             # any line between request line and here is a header in `Name: Value` format
         * optional body here               # every line after the blank line, until the end of the request is the body
         */

        String[] lineSplit = raw.split("\r\n");
        // ex: [POST /submit HTTP/1.1, Host: localhost, Content-Type: application/json, Content-Length: 13, , {"key":"val"}]

        // get method, path, httpVersion from the first line
        String requestInfo = lineSplit[0].strip();

        String[] parts = requestInfo.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException();
        }

        // gets method type by getting substring to first space in info line
        String methodStr = parts[0];
        HttpMethod method = getHttpMethod(methodStr);

        // gets filepath by getting substring from first space to "HTTP"
        String path = parts[1];

        // get httpVersion
        String httpVersion = parts[2];

        int endHeaderIndex = -1;
        // get all headers
        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lineSplit.length; i++) {
            // end of the header section has been reached
            if (lineSplit[i].isEmpty()) {
                endHeaderIndex = i;
                break;
            }

            // parse header as KEY: VALUE
            int colon = lineSplit[i].indexOf(": ");
            String key = lineSplit[i].substring(0, colon);
            String value = lineSplit[i].substring(colon + 2);

            headers.put(key, value);
        }

        // every line after this, if it exists, is the body
        StringBuilder body = new StringBuilder();
        if (endHeaderIndex != -1 && endHeaderIndex + 1 < lineSplit.length) {
            for (int i = endHeaderIndex + 1; i < lineSplit.length; i++) {
                if (i > endHeaderIndex + 1) {
                    body.append("\r\n");
                }

                body.append(lineSplit[i]);
            }
        }

        // construct HttpRequest object from parsed information
        return new HttpRequest(
            method,
            path,
            headers,
            body.toString(),
            httpVersion
        );
    }

    private static HttpMethod getHttpMethod(String methodStr) {
        return switch (methodStr.toUpperCase()) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "DELETE" -> HttpMethod.DELETE;
            default -> throw new IllegalArgumentException();
        };
    }
}
