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
 * Currently requires testing individually with KEY as env var
 * JUnit tests + a simple main() runner for OpenAiApiAdapter.
 *
 * How to run the MAIN (real API):
 *   # macOS/Linux
 *   export OPENAI_API_KEY=sk-...your real key...
 *   mvn -q -DskipTests exec:java -Dexec.mainClass="interface_adapter.generate_feedback.OpenAiApiAdapterTest"
 *
 *   # Windows PowerShell
 *   $env:OPENAI_API_KEY="sk-...your real key..."
 *   mvn -q -DskipTests exec:java -Dexec.mainClass="interface_adapter.generate_feedback.OpenAiApiAdapterTest"
 *
 * Optional: pass a custom prompt as args to main:
 *   -Dexec.args="Write a one-line poem about unit tests."
 *
 * How to run the MAIN in MOCK mode (no real key needed):
 *   mvn -q -DskipTests exec:java -Dexec.mainClass="interface_adapter.generate_feedback.OpenAiApiAdapterTest" -Dexec.args="--mock"
 *
 * NOTE: The adapter reads the API key from the OS environment variable OPENAI_API_KEY (System.getenv),
 *       which cannot be set from inside Java. You must export it in your shell / IDE Run Configuration.
 */
class OpenAiApiAdapterTest {

    private MockWebServer server;
    private String originalBaseUrlProp;

    @BeforeEach
    void startServer() throws IOException {
        // Most tests use MockWebServer; set up and redirect the adapter's base URL.
        server = new MockWebServer();
        server.start();
        originalBaseUrlProp = System.getProperty("OPENAI_API_BASE_URL");
        System.setProperty("OPENAI_API_BASE_URL", server.url("/v1/chat/completions").toString());
    }

    @AfterEach
    void shutdownServer() throws IOException {
        if (server != null) server.shutdown();
        if (originalBaseUrlProp == null) {
            System.clearProperty("OPENAI_API_BASE_URL");
        } else {
            System.setProperty("OPENAI_API_BASE_URL", originalBaseUrlProp);
        }
    }

    // ---------- Constructor behavior when env var is missing ----------

    @Test
    @DisabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
    void constructorThrows_whenApiKeyMissing() {
        var ex = assertThrows(NullPointerException.class, OpenAiApiAdapter::new);
        assertTrue(ex.getMessage().contains("OPENAI_API_KEY"), "Expected message to mention OPENAI_API_KEY");
    }

    // ---------- Happy path using MockWebServer (recommended for unit tests) ----------

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void callGeneralAnalysis_sendsProperPayload_andParsesContent() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("""
                    {
                      "choices":[
                        {"message":{"content":"Hello from mock!"}}
                      ]
                    }
                    """)
                .addHeader("Content-Type", "application/json"));

        // We only need the key to construct the adapter; any non-empty value is fine for this mocked test.
        OpenAiApiAdapter adapter = new OpenAiApiAdapter();

        String result = adapter.callGeneralAnalysis("Say hi");
        assertEquals("Hello from mock!", result);

        RecordedRequest req = server.takeRequest();
        assertEquals("/v1/chat/completions", req.getPath());
        assertEquals("POST", req.getMethod());
        assertNotNull(req.getHeader("Authorization"));
        assertTrue(req.getHeader("Authorization").startsWith("Bearer "));
        String sent = req.getBody().readUtf8();
        assertTrue(sent.contains("\"model\":\"gpt-4o-mini\""));
        assertTrue(sent.contains("\"temperature\":0.5"));
        assertTrue(sent.contains("\"content\":\"Say hi\""));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void callCorrelationBayes_and_callRecommendation_delegateToSameChat() throws Exception {
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

        assertTrue(server.takeRequest().getBody().readUtf8().contains("correlation prompt"));
        assertTrue(server.takeRequest().getBody().readUtf8().contains("recommendation prompt"));
    }

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
        assertTrue(ex.getMessage().contains("GPT API error 400"));
        assertTrue(ex.getMessage().contains("Bad request"));
    }

    // ======================================================================
    // A simple MAIN so you can quickly run with a real key or in mock mode.
    // ======================================================================

    public static void main(String[] args) throws Exception {
        // Args: [--mock] [prompt...]
        boolean mockMode = args != null && args.length > 0 && "--mock".equalsIgnoreCase(args[0]);
        String prompt = "Write a haiku about unit testing in Java.";
        if (args != null && args.length > (mockMode ? 1 : 0)) {
            prompt = String.join(" ", java.util.Arrays.copyOfRange(args, mockMode ? 1 : 0, args.length));
        }

        String envKey = System.getenv("OPENAI_API_KEY");
        System.out.println("=== OpenAiApiAdapter Test Runner ===");
        System.out.println("OPENAI_API_KEY present? " + (envKey != null && !envKey.isBlank()));
        if (envKey != null && envKey.length() >= 8) {
            System.out.println("Key preview: " + envKey.substring(0, 4) + "..." + envKey.substring(envKey.length() - 4));
        }

        if (mockMode) {
            // Run against a mock server (no real key needed, but constructor still checks env var).
            // If you don't want to set a real key, temporarily export a dummy one:
            //   export OPENAI_API_KEY=dummy   (or on Windows: $env:OPENAI_API_KEY="dummy")
            try (MockWebServer server = new MockWebServer()) {
                server.start();
                System.setProperty("OPENAI_API_BASE_URL", server.url("/v1/chat/completions").toString());

                server.enqueue(new MockResponse()
                        .setResponseCode(200)
                        .setBody("""
                            {"choices":[{"message":{"content":"[MOCK] Hello from MockWebServer!"}}]}
                            """)
                        .addHeader("Content-Type", "application/json"));

                OpenAiApiAdapter adapter = new OpenAiApiAdapter();
                String out = adapter.callGeneralAnalysis(prompt);
                System.out.println("Mocked response:\n" + out);

                server.shutdown();
                System.clearProperty("OPENAI_API_BASE_URL");
            }
            System.out.println("=== Done (mock mode) ===");
            return;
        }

        // Real call path: requires OPENAI_API_KEY in your environment.
        if (envKey == null || envKey.isBlank()) {
            System.err.println("ERROR: OPENAI_API_KEY is not set in your environment.");
            System.err.println("Set it and re-run this main. Examples:");
            System.err.println("  macOS/Linux : export OPENAI_API_KEY=sk-...");
            System.err.println("  Windows PS  : $env:OPENAI_API_KEY=\"sk-...\"");
            System.exit(2);
        }

        // Optional: you can override the endpoint via -DOPENAI_API_BASE_URL, but by default it's the real API.
        String endpoint = System.getProperty("OPENAI_API_BASE_URL",
                "https://api.openai.com/v1/chat/completions");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Prompt  : " + prompt);

        try {
            OpenAiApiAdapter adapter = new OpenAiApiAdapter();
            String general = adapter.callGeneralAnalysis(prompt);
            System.out.println("\n[callGeneralAnalysis]\n" + general + "\n");

            String corr = adapter.callCorrelationBayes("Explain correlation vs. causation briefly.");
            System.out.println("[callCorrelationBayes]\n" + corr + "\n");

            String reco = adapter.callRecommendation("Recommend 3 tips to improve unit tests.");
            System.out.println("[callRecommendation]\n" + reco + "\n");

            System.out.println("=== Done (real call) ===");
        } catch (IOException e) {
            System.err.println("I/O error calling API: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
    }
}
