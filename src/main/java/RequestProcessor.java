import java.io.IOException;
import java.net.Socket;

public class RequestProcessor implements Runnable {

    private Socket socket;
    private Router router;

    public RequestProcessor(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            // open new connection through Socket
            ConnectionHandler handler = new ConnectionHandler(socket);

            // get raw request from ConnectionHandler object
            String rawRequest = handler.readRequest();

            // parse into an HttpRequest object
            HttpRequest request = HttpRequestParser.parse(rawRequest);

            // use router to turn request into an HttpResponse object
            HttpResponse response = router.dispatch(request);

            // serialize response into a String and send back to ConnectionHandler
            handler.sendResponse(response.serialize());

            // end connection
            handler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
