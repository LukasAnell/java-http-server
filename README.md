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

**Compile sources:**
```bash
mvn compile
```

**Run tests:**
```bash
mvn test
```

---

## HTTP Request Format

The parser (HttpRequestParser) can handle standard HTTP/1.1 requests:

```
GET /index.html HTTP/1.1\r\n
Host: localhost\r\n
Connection: keep-alive\r\n
\r\n
```

```
POST /submit HTTP/1.1\r\n
Host: localhost\r\n
Content-Type: application/json\r\n
Content-Length: 13\r\n
\r\n
{"key":"val"}
```

- The first line in the HTTP message is the request line. Format: `METHOD PATH HTTP_VERSION`
- Every remaining line until the blank line is a header. Format: `Name: Value`
- Everything following the blank line is the body.

---

## Supported MIME Types

| Extension | MIME Type |
|---|---|
| `.html` | `text/html` |
| `.css` | `text/css` |
| `.js` | `application/javascript` |
| `.json` | `application/json` |
| `.txt` | `text/plain` |
| anything else | `application/octet-stream` |

---

## Design Decisions

**Difference between 404 and 405 in Router** \
When a request is received, the router first checks if the path exists, then if the method is registered for that path. This is used to differentiate between `404 Not Found` errors vs. `405 Method Not Allowed` errors, rather than returning a 404 error for everything unmatched.

**`HttpResponseBuilder` uses the builder pattern** \
Responses can be easily constructed using the methods in `HttpResponseBuilder` with the builder pattern. This makes it easy to build responses incrementally while only having to set the fields that will be relevant to the desired response.

---

## Limitations

- No HTTPS support
- Only `GET`, `POST`, `PUT`, and `DELETE` methods are supported as of now
- Reading the request body doesn't take `Content-Length` headers into account, which could lead to errors
- No support for path parameters or wildcard routing (/*), paths can only be exact matches
- No session management or cookie support
- No protection against path traversal attacks (e.g. `../../etc/passwd`)
- Only text-based file serving is supported (no binary files like images or PDFs)

---

## Future Updates

- Add HTTPS support with `SSLServerSocket` class
- Correctly use the `Content-Length` header when reading request bodies
- Add path parameter routing. For example: `/users/:id`
- Protection against path traversal attacks
- Support binary file serving
- Add simple session management with cookies
- Add some kind of middleware support for logging, authentication, and error handling (not planned right now)

---
