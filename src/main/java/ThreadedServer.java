import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedServer {

    private Server server;
    private ExecutorService executorService;
    private Router router;
    private volatile boolean isStopped = false;

    public ThreadedServer(int port, int threadPoolSize, Router router)
        throws IOException {
        this.server = new Server(port);
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.router = router;
    }

    public void start() {
        // server is started, continue looping as long as isStopped is false
        while (!isStopped) {
            try {
                // try to make a connection with a client
                Socket clientSocket = server.acceptConnection();

                // add RequestProcessor object with newly connected client to the thread pool
                executorService.execute(
                    new RequestProcessor(clientSocket, router)
                );
            } catch (IOException e) {
                if (isStopped) {
                    // server is stopped, can break out of while loop now
                    break;
                }

                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        isStopped = true;

        try {
            // unblocks the acceptConnection() in start()
            server.close();
        } finally {
            // close the thread pool once stop() is called
            executorService.shutdown();
        }
    }

    public boolean isRunning() {
        return (
            server.isRunning() &&
            (!executorService.isShutdown() && !executorService.isTerminated())
        );
    }
}
