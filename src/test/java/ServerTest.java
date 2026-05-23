import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ServerTest {

    private Server server;

    @AfterEach
    void tearDown() throws IOException {
        if (server != null && server.isRunning()) {
            server.close();
        }
    }

    @Test
    void testServerStartsOnPort() throws IOException {
        server = new Server(8080);
        assertEquals(8080, server.getPort());
    }

    @Test
    void testServerIsRunningAfterStart() throws IOException {
        server = new Server(8080);
        assertTrue(server.isRunning());
    }

    @Test
    void testServerIsNotRunningAfterClose() throws IOException {
        server = new Server(8080);
        server.close();
        assertFalse(server.isRunning());
    }

    @Test
    void testServerBindsToPort() throws IOException {
        server = new Server(8081);
        // if the port is in use this would throw — the fact it doesn't means it bound
        assertTrue(server.isRunning());
    }

    @Test
    void testConnectionHandlerReadAndRespond()
        throws IOException, InterruptedException {
        server = new Server(8082);

        Thread serverThread = new Thread(() -> {
            try {
                Socket client = server.acceptConnection();
                ConnectionHandler handler = new ConnectionHandler(client);
                String request = handler.readRequest();
                handler.sendResponse("HTTP/1.1 200 OK\r\n\r\nHello");
                handler.close();
            } catch (IOException e) {
                fail("Server thread threw: " + e.getMessage());
            }
        });

        serverThread.start();

        // give server thread time to start
        Thread.sleep(100);

        Socket clientSocket = new Socket("localhost", 8082);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream())
        );

        out.print("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");
        out.flush();

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        clientSocket.close();
        serverThread.join(1000);

        assertTrue(response.toString().contains("200 OK"));
    }

    @Test
    void testConnectionHandlerSendsResponse()
        throws IOException, InterruptedException {
        server = new Server(8083);

        Thread serverThread = new Thread(() -> {
            try {
                Socket client = server.acceptConnection();
                ConnectionHandler handler = new ConnectionHandler(client);
                handler.readRequest();
                handler.sendResponse("HTTP/1.1 200 OK\r\n\r\nHello World");
                handler.close();
            } catch (IOException e) {
                fail("Server thread threw: " + e.getMessage());
            }
        });

        serverThread.start();
        Thread.sleep(100);

        Socket clientSocket = new Socket("localhost", 8083);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream())
        );

        out.print("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");
        out.flush();

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        clientSocket.close();
        serverThread.join(1000);

        assertTrue(response.toString().contains("Hello World"));
    }
}
