import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileHandler {

    public static HttpResponse handle(
        HttpRequest request,
        String rootDirectory
    ) {
        // deny any request that's not GET
        if (request.getMethod() != HttpMethod.GET) {
            HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

            return new HttpResponse(
                status,
                new HashMap<>(),
                status.getReason()
            );
        }

        // initialize HttpResponse constructor arguments to be filled later
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, String> headers = new HashMap<>();
        StringBuilder body = new StringBuilder();

        StringBuilder fullPath = new StringBuilder(rootDirectory);
        // check if request's path is /
        if (request.getPath().equals("/")) {
            // desired file is /rootDirectory/index.html
            fullPath.append("/index.html");
        } else {
            // getPath is the requested file
            fullPath.append(request.getPath());
        }

        try (Scanner reader = new Scanner(new File(fullPath.toString()))) {
            // change status to indicate that the file was found
            status = HttpStatus.OK;

            // add headers for Content-Type and Content-Length
            String mimeType = MimeTypes.getType(fullPath.toString());
            headers.put("Content-Type", mimeType);

            long fileSize = new File(fullPath.toString()).length();
            headers.put("Content-Length", Long.toString(fileSize));

            // append each line in file to body
            while (reader.hasNextLine()) {
                body.append(reader.nextLine()).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            return new HttpResponse(
                status,
                new HashMap<>(),
                status.getReason()
            );
        }

        return new HttpResponse(status, headers, body.toString());
    }
}
