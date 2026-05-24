public class MimeTypes {

    public static String getType(String filePath) {
        // returns the MIME type for a given file based on its file extension

        // find index of last .
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex == -1) {
            return "application/octet-stream";
        }

        // get extension string
        String extension = filePath.substring(dotIndex + 1);

        // match extensionStr to its MIME type, returning a default value if it's not one of: html, css, js, json, txt
        return switch (extension.toUpperCase()) {
            case "HTML" -> "text/html";
            case "CSS" -> "text/css";
            case "JS" -> "application/javascript";
            case "JSON" -> "application/json";
            case "TXT" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
