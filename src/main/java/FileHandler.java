import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    public static HttpResponse handle(
        HttpRequest request,
        String rootDirectory
    ) {
        // deny any request that's not GET
        if (request.getMethod() != HttpMethod.GET) {
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .addHeader("Content-Type", "text/plain")
                .setBody("405 Method Not Allowed")
                .build();
        }

        // build the filepath based on whether or not / was requested
        String filePath = request.getPath().equals("/")
            ? "/index.html"
            : request.getPath();

        Path path = Path.of(rootDirectory, filePath);

        if (!Files.exists(path)) {
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.NOT_FOUND)
                .addHeader("Content-Type", "text/plain")
                .setBody("404 Not Found")
                .build();
        }

        try {
            // add lines of file to body
            String body = Files.readString(path, StandardCharsets.UTF_8);

            // add headers for Content-Type and Content-Length
            String mimeType = MimeTypes.getType(filePath);
            String contentLength = String.valueOf(
                body.getBytes(StandardCharsets.UTF_8).length
            );

            // construct HttpResponse
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .addHeader("Content-Type", mimeType)
                .addHeader("Content-Length", contentLength)
                .setBody(body)
                .build();
        } catch (IOException e) {
            return new HttpResponseBuilder()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setBody("500 Internal Server Error")
                .build();
        }
    }
}
