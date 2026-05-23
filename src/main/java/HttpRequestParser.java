import java.util.Arrays;
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

        // gets method type by getting substring to first space in info line
        String methodStr = requestInfo.substring(0, requestInfo.indexOf(" "));
        HttpMethod method = getHttpMethod(methodStr);

        // gets filepath by getting substring from first space to "HTTP"
        String path = requestInfo.substring(
            requestInfo.indexOf(" "),
            requestInfo.indexOf("HTTP")
        );

        // get httpVersion
        String httpVersion = requestInfo.substring(requestInfo.indexOf("HTTP"));

        return null;
    }

    private static HttpMethod getHttpMethod(String methodStr) {
        switch (methodStr.toUpperCase()) {
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            case "PUT":
                return HttpMethod.PUT;
            case "DELETE":
                return HttpMethod.DELETE;
            default:
                return null;
        }
    }
}
