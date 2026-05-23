import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConnectionHandler {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ConnectionHandler(Socket socket) throws IOException {
        // set up buffered input and output streams from socket
        this.socket = socket;
        this.in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        this.out = new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream())
        );
    }

    public String readRequest() throws IOException {
        StringBuilder request = new StringBuilder();

        String line;
        // breaks the loop if it reaches the end of the request
        // \r\n\r\n is turned into an empty String by readLine(), so checking isEmpty() is enough
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            request.append(line).append("\r\n");
        }

        // append final blank line of HTTP request
        request.append("\r\n");

        return request.toString();
    }

    public void sendResponse(String response) throws IOException {
        //
    }

    public void close() throws IOException {}
}
