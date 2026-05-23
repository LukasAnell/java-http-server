public class MimeTypes {

    public static String getType(String filePath) {
        // returns the MIME type for a given file based on its file extension

        // split filePath by "." then find the last entry to find the file extension?
        String[] filePathSplit = filePath.split(".");
        String extensionStr = filePathSplit[filePathSplit.length - 1];

        // match extensionStr to its MIME type, returning a default value if it's not one of: html, css, js, json, txt
        return switch (extensionStr.toUpperCase()) {
            case "HTML" -> "text/html";
            case "CSS" -> "text/css";
            case "JS" -> "application/javascript";
            case "JSON" -> "application/json";
            case "TXT" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
