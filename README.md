# java-http-server

---

## Project Summary

This reposistory contains an incomplete implementation of a multithreaded HTTP/1.1 web server in Java. My goal with this project was to handle browser requests, parse and constructor HTTP messages manually, serve static files, route requests to specific handlers, and process multiple connections at once using a thread pool.

---

## Features

- Uses java.net.ServerSocket and java.net.Socket to run a raw socket server
- Can parse HTTP/1.1 requests into method, path, headers, and a body
- Easy HTTP response building with the HttpResponseBuilder class
- MIME types supported: text/html, text/css, application/javascript, application/json, text/plain
    - application/octet-stream used as default value for unsupported MIME types
- Static file serving with correct MIME types and `Content-Length` headers
- Request routing that efficiently maps method and map combinations to specific handlers
- Multithreaded request handling through `ExecutorService`

---

## Project Structure

```
❯ tree --dirsfirst
.
├── lib
│   └── junit-platform-console-standalone-1.10.2.jar
├── src
│   ├── main
│   │   └── java
│   │       ├── ConnectionHandler.java
│   │       ├── FileHandler.java
│   │       ├── Handler.java
│   │       ├── HttpMethod.java
│   │       ├── HttpRequest.java
│   │       ├── HttpRequestParser.java
│   │       ├── HttpResponse.java
│   │       ├── HttpResponseBuilder.java
│   │       ├── HttpStatus.java
│   │       ├── Main.java
│   │       ├── MimeTypes.java
│   │       ├── RequestProcessor.java
│   │       ├── Router.java
│   │       ├── Server.java
│   │       └── ThreadedServer.java
│   └── test
│       └── java
│           ├── FileHandlerTest.java
│           ├── HttpRequestParserTest.java
│           ├── HttpResponseTest.java
│           ├── RouterTest.java
│           ├── ServerTest.java
│           └── ThreadedServerTest.java
├── LICENSE
├── pom.xml
└── README.md
```

---

## How to Build and Run

### With Maven

---

## HTTP Request Format

---

## Supported MIME Types

---

## Design Decisions

---

## Limitations

---

## Future Updates

---
