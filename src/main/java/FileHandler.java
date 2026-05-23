public class FileHandler {

    public static HttpResponse handle(
        HttpRequest request,
        String rootDirectory
    ) {
        // serve a file from rootDirectory based on the path specified in request

        // find full file path
        // if the path is just /, serve index.html from the rootDirectory

        // if the file exists and is readable, return a response saying "200 OK"
        // include 2 headers: Content-Type: MIME type, Content-Length: file size in bytes
        //  file contents as the body of the response

        // if the file doesn't exist, return a 404 Not Found response
        // body is "404 Not Found"

        // Only handle GET requests, returning 405 Method Not Allowed for anything else

        return null;
    }
}
