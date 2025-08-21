// File: src/test/java/interface_adapter/generate_feedback/OpenAiApiAdapterTest.java
package interface_adapter.generate_feedback;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link OpenAiApiAdapter}.
 *
 * Notes:
 * - These tests use OkHttp's MockWebServer to avoid real network calls.
 * - We conditionally enable the "happy path" tests only when OPENAI_API_KEY is set,
 *   because the adapter's constructor requires it. (In CI, configure that env var
 *   to any non-empty dummy string.)
 * - We also include a test that verifies constructor failure when the env var is absent,
 *   which is conditionally disabled when the env var is present.
 */
class OpenAiApiAdapterTest {

    private MockWebServer server;
    private String originalBaseUrlProp;

    @BeforeEach
    void startServer() throws IOException {
        server = new MockWebServer();
        server.start();
        // Save and override the system property used by the adapter
        originalBaseUrlProp = System.getProperty("OPENAI_API_BASE_URL");
        System.setProperty("OPENAI_API_BASE_URL", server.url("/v1/chat/completions").toString());
    }

    @AfterEach
    void shutdownServer() throws IOException {
        if (server != null) {
            server.shutdown();
        }
        // Restore the system property
        if (originalBaseUrlProp == null) {
            System.clearProperty("OPENAI_API_BASE_URL");
        } else {
            System.setProperty("OPENAI_API_BASE_URL", originalBaseUrlProp);
        }
    }

    // ---------- Constructor behavior when env var is missing ----------

    /**
     * This test verifies the constructor fails if OPENAI_API_KEY is not set.
     * It is disabled when the env var IS set (e.g., in CI), to avoid false failures.
     */
    @Test
    @DisabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
    void constructorThrows_whenApiKeyMissing() {
        NullPointerException ex = assertThrows(NullPointerException.class, OpenAiApiAdapter::new);
        assertTrue(ex.getMessage().contains("OPENAI_API_KEY"), "Expected message to mention OPENAI_API_KEY");
    }

    // ---------- Happy path + request/response parsing (requires env var present) ----------

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void callGeneralAnalysis_sendsProperPayload_andParsesContent() throws Exception {
        // Arrange: mock a successful OpenAI-like response
        String body = """
            {
              "id":"chatcmpl-test",
              "object":"chat.completion",
              "choices":[
                {"index":0,"message":{"role":"assistant","content":"Hello there!"},"finish_reason":"stop"}
              ]
            }
            """;
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        OpenAiApiAdapter adapter = new OpenAiApiAdapter();

        // Act
        String result = adapter.callGeneralAnalysis("Say hi");

        // Assert response content
        assertEquals("Hello there!", result);

        // Inspect the request that the adapter sent
        RecordedRequest req = server.takeRequest();
        assertEquals("/v1/chat/completions", req.getPath());
        assertEquals("POST", req.getMethod());
        assertNotNull(req.getHeader("Authorization"));
        assertTrue(req.getHeader("Authorization").startsWith("Bearer "),
                "Authorization header should be Bearer <OPENAI_API_KEY>");

        String sent = req.getBody().readUtf8();
        // Minimal checks: model, temperature, message content presence
        assertTrue(sent.contains("\"model\":\"gpt-4o-mini\""), "Model should be gpt-4o-mini");
        assertTrue(sent.contains("\"temperature\":0.5"), "Temperature should be 0.5 (Constants.HALF)");
        assertTrue(sent.contains("\"role\":\"user\""), "Should send a user role message");
        assertTrue(sent.contains("\"content\":\"Say hi\""), "Should include the user prompt");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void callCorrelationBayes_and_callRecommendation_delegateToSameChat() throws Exception {
        // Queue two successful responses (one per call)
        server.enqueue(new MockResponse().setResponseCode(200).setBody("""
            {"choices":[{"message":{"content":"Corr OK"}}]}
            """).addHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("""
            {"choices":[{"message":{"content":"Reco OK"}}]}
            """).addHeader("Content-Type", "application/json"));

        OpenAiApiAdapter adapter = new OpenAiApiAdapter();

        String r1 = adapter.callCorrelationBayes("correlation prompt");
        assertEquals("Corr OK", r1);

        String r2 = adapter.callRecommendation("recommendation prompt");
        assertEquals("Reco OK", r2);

        // Verify first request had the first prompt, etc.
        RecordedRequest req1 = server.takeRequest();
        String b1 = req1.getBody().readUtf8();
        assertTrue(b1.contains("correlation prompt"));

        RecordedRequest req2 = server.takeRequest();
        String b2 = req2.getBody().readUtf8();
        assertTrue(b2.contains("recommendation prompt"));
    }

    // ---------- Error handling ----------

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void callGeneralAnalysis_propagatesHttpErrorsWithBody() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"error\":\"Bad request\"}")
                .addHeader("Content-Type", "application/json"));

        OpenAiApiAdapter adapter = new OpenAiApiAdapter();

        IOException ex = assertThrows(IOException.class,
                () -> adapter.callGeneralAnalysis("bad"));
        assertTrue(ex.getMessage().contains("GPT API error 400"),
                "Should include status code in exception message");
        assertTrue(ex.getMessage().contains("Bad request"),
                "Should include response body in exception message");
    }
}
