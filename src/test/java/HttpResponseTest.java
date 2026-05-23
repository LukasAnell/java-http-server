import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

    // --- HttpStatus ---

    @Test
    void testStatusCode() {
        assertEquals(200, HttpStatus.OK.getCode());
    }

    @Test
    void testStatusReason() {
        assertEquals("Not Found", HttpStatus.NOT_FOUND.getReason());
    }

    @Test
    void testStatusCodes() {
        assertEquals(201, HttpStatus.CREATED.getCode());
        assertEquals(400, HttpStatus.BAD_REQUEST.getCode());
        assertEquals(405, HttpStatus.METHOD_NOT_ALLOWED.getCode());
        assertEquals(500, HttpStatus.INTERNAL_SERVER_ERROR.getCode());
    }

    // --- HttpResponse ---

    @Test
    void testResponseStatus() {
        HttpResponse response = new HttpResponse(
            HttpStatus.OK,
            Map.of("Content-Type", "text/html"),
            "Hello"
        );
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testResponseBody() {
        HttpResponse response = new HttpResponse(
            HttpStatus.OK,
            Map.of(),
            "Hello World"
        );
        assertEquals("Hello World", response.getBody());
    }

    @Test
    void testSerializeStatusLine() {
        HttpResponse response = new HttpResponse(HttpStatus.OK, Map.of(), "");
        assertTrue(response.serialize().startsWith("HTTP/1.1 200 OK\r\n"));
    }

    @Test
    void testSerializeHeaders() {
        HttpResponse response = new HttpResponse(
            HttpStatus.OK,
            Map.of("Content-Type", "text/plain"),
            ""
        );
        assertTrue(
            response.serialize().contains("Content-Type: text/plain\r\n")
        );
    }

    @Test
    void testSerializeBody() {
        HttpResponse response = new HttpResponse(
            HttpStatus.OK,
            Map.of(),
            "Hello World"
        );
        assertTrue(response.serialize().endsWith("\r\n\r\nHello World"));
    }

    @Test
    void testSerializeEmptyBody() {
        HttpResponse response = new HttpResponse(
            HttpStatus.NOT_FOUND,
            Map.of(),
            ""
        );
        assertTrue(response.serialize().endsWith("\r\n\r\n"));
    }

    // --- HttpResponseBuilder ---

    @Test
    void testBuilderDefaultStatus() {
        HttpResponse response = new HttpResponseBuilder().build();
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testBuilderSetStatus() {
        HttpResponse response = new HttpResponseBuilder()
            .setStatus(HttpStatus.NOT_FOUND)
            .build();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testBuilderAddHeader() {
        HttpResponse response = new HttpResponseBuilder()
            .addHeader("Content-Type", "text/html")
            .build();
        assertEquals("text/html", response.getHeaders().get("Content-Type"));
    }

    @Test
    void testBuilderSetBody() {
        HttpResponse response = new HttpResponseBuilder()
            .setBody("Hello")
            .build();
        assertEquals("Hello", response.getBody());
    }

    @Test
    void testBuilderChaining() {
        HttpResponse response = new HttpResponseBuilder()
            .setStatus(HttpStatus.CREATED)
            .addHeader("Content-Type", "application/json")
            .setBody("{\"id\": 1}")
            .build();
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(
            "application/json",
            response.getHeaders().get("Content-Type")
        );
        assertEquals("{\"id\": 1}", response.getBody());
    }

    @Test
    void testBuilderMultipleHeaders() {
        HttpResponse response = new HttpResponseBuilder()
            .addHeader("Content-Type", "text/html")
            .addHeader("Connection", "close")
            .build();
        assertEquals(2, response.getHeaders().size());
    }
}
