import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileHandlerTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("filehandler_test");

        Files.writeString(tempDir.resolve("index.html"), "<h1>Hello</h1>");
        Files.writeString(tempDir.resolve("about.html"), "<h1>About</h1>");
        Files.writeString(tempDir.resolve("style.css"), "body { margin: 0; }");
        Files.writeString(tempDir.resolve("data.json"), "{\"key\": \"value\"}");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(p -> p.toFile().delete());
    }

    private HttpRequest get(String path) {
        return new HttpRequest(HttpMethod.GET, path, Map.of(), "", "HTTP/1.1");
    }

    private HttpRequest post(String path) {
        return new HttpRequest(HttpMethod.POST, path, Map.of(), "", "HTTP/1.1");
    }

    // --- MimeTypes ---

    @Test
    void testMimeHtml() {
        assertEquals("text/html", MimeTypes.getType("index.html"));
    }

    @Test
    void testMimeCss() {
        assertEquals("text/css", MimeTypes.getType("style.css"));
    }

    @Test
    void testMimeJs() {
        assertEquals("application/javascript", MimeTypes.getType("app.js"));
    }

    @Test
    void testMimeJson() {
        assertEquals("application/json", MimeTypes.getType("data.json"));
    }

    @Test
    void testMimeTxt() {
        assertEquals("text/plain", MimeTypes.getType("readme.txt"));
    }

    @Test
    void testMimeUnknown() {
        assertEquals("application/octet-stream", MimeTypes.getType("file.xyz"));
    }

    // --- FileHandler ---

    @Test
    void testServeIndexHtml() {
        HttpResponse response = FileHandler.handle(
            get("/"),
            tempDir.toString()
        );
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("<h1>Hello</h1>", response.getBody());
    }

    @Test
    void testServeNamedFile() {
        HttpResponse response = FileHandler.handle(
            get("/about.html"),
            tempDir.toString()
        );
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("<h1>About</h1>", response.getBody());
    }

    @Test
    void testContentTypeHeader() {
        HttpResponse response = FileHandler.handle(
            get("/style.css"),
            tempDir.toString()
        );
        assertEquals("text/css", response.getHeaders().get("Content-Type"));
    }

    @Test
    void testContentLengthHeader() {
        HttpResponse response = FileHandler.handle(
            get("/index.html"),
            tempDir.toString()
        );
        assertNotNull(response.getHeaders().get("Content-Length"));
    }

    @Test
    void testNotFoundResponse() {
        HttpResponse response = FileHandler.handle(
            get("/missing.html"),
            tempDir.toString()
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("404 Not Found", response.getBody());
    }

    @Test
    void testMethodNotAllowed() {
        HttpResponse response = FileHandler.handle(
            post("/index.html"),
            tempDir.toString()
        );
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    void testJsonContentType() {
        HttpResponse response = FileHandler.handle(
            get("/data.json"),
            tempDir.toString()
        );
        assertEquals(
            "application/json",
            response.getHeaders().get("Content-Type")
        );
    }
}
