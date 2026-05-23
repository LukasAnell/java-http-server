import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HttpRequestParserTest {

    private static final String SIMPLE_GET =
        "GET /index.html HTTP/1.1\r\n" +
        "Host: localhost\r\n" +
        "Connection: keep-alive\r\n" +
        "\r\n";

    private static final String POST_WITH_BODY =
        "POST /submit HTTP/1.1\r\n" +
        "Host: localhost\r\n" +
        "Content-Type: application/json\r\n" +
        "Content-Length: 13\r\n" +
        "\r\n" +
        "{\"key\":\"val\"}";

    private static final String DELETE_REQUEST =
        "DELETE /resource/1 HTTP/1.1\r\n" + "Host: localhost\r\n" + "\r\n";

    // --- Method ---

    @Test
    void testParsesGetMethod() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals(HttpMethod.GET, req.getMethod());
    }

    @Test
    void testParsesPostMethod() {
        HttpRequest req = HttpRequestParser.parse(POST_WITH_BODY);
        assertEquals(HttpMethod.POST, req.getMethod());
    }

    @Test
    void testParsesDeleteMethod() {
        HttpRequest req = HttpRequestParser.parse(DELETE_REQUEST);
        assertEquals(HttpMethod.DELETE, req.getMethod());
    }

    // --- Path ---

    @Test
    void testParsesPath() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals("/index.html", req.getPath());
    }

    @Test
    void testParsesPathWithResource() {
        HttpRequest req = HttpRequestParser.parse(DELETE_REQUEST);
        assertEquals("/resource/1", req.getPath());
    }

    // --- HTTP Version ---

    @Test
    void testParsesHttpVersion() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals("HTTP/1.1", req.getHttpVersion());
    }

    // --- Headers ---

    @Test
    void testParsesHeaders() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals("localhost", req.getHeaders().get("Host"));
    }

    @Test
    void testParsesMultipleHeaders() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals(2, req.getHeaders().size());
    }

    @Test
    void testParsesContentTypeHeader() {
        HttpRequest req = HttpRequestParser.parse(POST_WITH_BODY);
        assertEquals("application/json", req.getHeaders().get("Content-Type"));
    }

    // --- Body ---

    @Test
    void testEmptyBodyForGet() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals("", req.getBody());
    }

    @Test
    void testParsesBody() {
        HttpRequest req = HttpRequestParser.parse(POST_WITH_BODY);
        assertEquals("{\"key\":\"val\"}", req.getBody());
    }

    // --- toString ---

    @Test
    void testToString() {
        HttpRequest req = HttpRequestParser.parse(SIMPLE_GET);
        assertEquals("GET /index.html HTTP/1.1", req.toString());
    }

    // --- Errors ---

    @Test
    void testMalformedRequestLineThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            HttpRequestParser.parse("BADREQUEST\r\n\r\n")
        );
    }

    @Test
    void testUnsupportedMethodThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            HttpRequestParser.parse("PATCH /index.html HTTP/1.1\r\n\r\n")
        );
    }
}
