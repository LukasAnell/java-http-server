import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RouterTest {

    private Router router;

    private HttpRequest request(HttpMethod method, String path) {
        return new HttpRequest(method, path, Map.of(), "", "HTTP/1.1");
    }

    @BeforeEach
    void setUp() {
        router = new Router();

        router.register(HttpMethod.GET, "/", req ->
            new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .setBody("Home")
                .build()
        );

        router.register(HttpMethod.GET, "/about", req ->
            new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .setBody("About")
                .build()
        );

        router.register(HttpMethod.POST, "/submit", req ->
            new HttpResponseBuilder()
                .setStatus(HttpStatus.CREATED)
                .setBody("Created")
                .build()
        );
    }

    // --- hasRoute ---

    @Test
    void testHasRouteRegistered() {
        assertTrue(router.hasRoute(HttpMethod.GET, "/"));
    }

    @Test
    void testHasRouteNotRegistered() {
        assertFalse(router.hasRoute(HttpMethod.DELETE, "/"));
    }

    @Test
    void testHasRouteUnknownPath() {
        assertFalse(router.hasRoute(HttpMethod.GET, "/unknown"));
    }

    // --- dispatch ---

    @Test
    void testDispatchGet() {
        HttpResponse response = router.dispatch(request(HttpMethod.GET, "/"));
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Home", response.getBody());
    }

    @Test
    void testDispatchPost() {
        HttpResponse response = router.dispatch(
            request(HttpMethod.POST, "/submit")
        );
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

    @Test
    void testDispatchNotFound() {
        HttpResponse response = router.dispatch(
            request(HttpMethod.GET, "/missing")
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testDispatchMethodNotAllowed() {
        // path exists but wrong method
        HttpResponse response = router.dispatch(
            request(HttpMethod.DELETE, "/")
        );
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    void testDispatchAboutPage() {
        HttpResponse response = router.dispatch(
            request(HttpMethod.GET, "/about")
        );
        assertEquals("About", response.getBody());
    }

    @Test
    void testRegisterMultipleMethodsSamePath() {
        router.register(HttpMethod.DELETE, "/about", req ->
            new HttpResponseBuilder()
                .setStatus(HttpStatus.OK)
                .setBody("Deleted")
                .build()
        );
        HttpResponse response = router.dispatch(
            request(HttpMethod.DELETE, "/about")
        );
        assertEquals("Deleted", response.getBody());
    }

    @Test
    void testDispatchCallsCorrectHandler() {
        HttpResponse getResponse = router.dispatch(
            request(HttpMethod.GET, "/about")
        );
        HttpResponse postResponse = router.dispatch(
            request(HttpMethod.POST, "/submit")
        );
        assertEquals("About", getResponse.getBody());
        assertEquals("Created", postResponse.getBody());
    }
}
