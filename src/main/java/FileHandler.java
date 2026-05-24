import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    public static HttpResponse handle(
        HttpRequest request,
        String rootDirectory
    ) {
        // deny any request that's not GET
        if (request.getMethod() != HttpMethod.GET) {
            HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

            return new HttpResponse(status, new HashMap<>(), "");
        }

        // initialize HttpResponse constructor arguments to be filled later
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, String> headers = new HashMap<>();
        String body;

        StringBuilder fullPath = new StringBuilder(rootDirectory);
        // check if request's path is /
        if (request.getPath().equals("/")) {
            // desired file is /rootDirectory/index.html
            fullPath.append("/index.html");
        } else {
            // getPath is the requested file
            fullPath.append(request.getPath());
        }

        Path path = Path.of(fullPath.toString());

        if (!Files.exists(path)) {
            return new HttpResponse(
                status,
                new HashMap<>(),
                status.getCode() + " " + status.getReason()
            );
        }

        try {
            // add lines of file to body
            body = Files.readString(path);

            // change status to indicate that the file was found
            status = HttpStatus.OK;

            // add headers for Content-Type and Content-Length
            String mimeType = MimeTypes.getType(fullPath.toString());
            headers.put("Content-Type", mimeType);

            long fileSize = new File(fullPath.toString()).length();
            headers.put("Content-Length", Long.toString(fileSize));
        } catch (IOException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new HttpResponse(
                status,
                new HashMap<>(),
                status.getCode() + " " + status.getReason()
            );
        }

        return new HttpResponse(status, headers, body.toString());
    }
}
