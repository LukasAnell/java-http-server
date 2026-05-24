import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ThreadedServerTest {

    private ThreadedServer server;

    private Router buildRouter() {
        Router router = new Router();
        router.register(HttpMethod.GET, "/", req ->
            new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .addHeader("Content-Type", "text/plain")
                .setBody("Hello")
                .build()
        );
        router.register(HttpMethod.GET, "/slow", req -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .setBody("Slow")
                .build();
        });
        return router;
    }

    @AfterEach
    void tearDown() throws IOException {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    private String sendRequest(int port, String raw) throws IOException {
        Socket socket = new Socket("localhost", port);
        socket.setSoTimeout(3000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        out.print(raw);
        out.flush();
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line).append("\n");
        }
        socket.close();
        return response.toString();
    }

    @Test
    void testServerRespondsToRequest() throws Exception {
        server = new ThreadedServer(9090, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);

        String response = sendRequest(
            9090,
            "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n"
        );
        assertTrue(response.contains("200 OK"));
    }

    @Test
    void testServerRespondsWithBody() throws Exception {
        server = new ThreadedServer(9091, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);

        String response = sendRequest(
            9091,
            "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n"
        );
        assertTrue(response.contains("Hello"));
    }

    @Test
    void testServerHandles404() throws Exception {
        server = new ThreadedServer(9092, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);

        String response = sendRequest(
            9092,
            "GET /missing HTTP/1.1\r\nHost: localhost\r\n\r\n"
        );
        assertTrue(response.contains("404"));
    }

    @Test
    void testConcurrentRequests() throws Exception {
        server = new ThreadedServer(9093, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);

        int numRequests = 4;
        ExecutorService clients = Executors.newFixedThreadPool(numRequests);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            futures.add(
                clients.submit(() ->
                    sendRequest(
                        9093,
                        "GET /slow HTTP/1.1\r\nHost: localhost\r\n\r\n"
                    )
                )
            );
        }

        long start = System.currentTimeMillis();
        for (Future<String> f : futures) {
            assertTrue(f.get(5, TimeUnit.SECONDS).contains("Slow"));
        }
        long elapsed = System.currentTimeMillis() - start;

        // 4 concurrent requests each taking 200ms should finish well under 800ms
        assertTrue(
            elapsed < 800,
            "Expected concurrent execution but took " + elapsed + "ms"
        );

        clients.shutdown();
    }

    @Test
    void testServerIsRunningAfterStart() throws Exception {
        server = new ThreadedServer(9094, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);
        assertTrue(server.isRunning());
    }

    @Test
    void testServerIsNotRunningAfterStop() throws Exception {
        server = new ThreadedServer(9095, 4, buildRouter());
        new Thread(() -> server.start()).start();
        Thread.sleep(100);
        server.stop();
        assertFalse(server.isRunning());
    }
}
