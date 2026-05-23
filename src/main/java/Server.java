import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private int port;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.port = port;
    }

    public Socket acceptConnection() throws IOException {
        return serverSocket.accept();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public boolean isRunning() {
        return serverSocket.isBound() && !serverSocket.isClosed();
    }

    public int getPort() {
        return port;
    }
}
