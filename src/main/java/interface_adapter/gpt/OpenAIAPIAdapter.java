package interface_adapter.gpt;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.generate_feedback.GPTService;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

public class OpenAIAPIAdapter implements GPTService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String MODEL = "gpt-4o-mini";
    private static final String ENDPOINT = System.getProperty(
            "OPENAI_API_BASE_URL",
            "https://api.openai.com/v1/chat/completions");

    private final OkHttpClient client;
    private final String API_KEY;

    public OpenAIAPIAdapter() {
        this.API_KEY = Objects.requireNonNull(System.getenv("OPENAI_API_KEY"), "OPENAI_API_KEY env variable not set.");

        this.client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(30))
                .build();

    }

    @Override public String callGeneralAnalysis(String prompt) throws IOException {
        return chat(prompt);
    }

    @Override public String callCorrelationBayes(String prompt) throws IOException {
        return chat(prompt);
    }

    @Override public String callRecommendation(String prompt) throws IOException {
        return chat(prompt);
    }

    /* --------------- Internal helper --------------- */

    private String chat(String userPrompt) throws IOException {
        JSONObject body = new JSONObject()
                .put("model", MODEL)
                .put("temperature", 0.5)
                .put("messages", new JSONArray()
                        .put(new JSONObject().put("role", "user").put("content", userPrompt)));

        Request req = new Request.Builder()
                .url(ENDPOINT)
                .header("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(body.toString(), JSON))
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException("GPT API error " + resp.code() + ": " + resp.body().string());
            }
            String json = resp.body().string(); // TODO: body is STILL nullable
            JSONArray choices = new JSONObject(json).getJSONArray("choices");
            return choices.getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        }
    }

}
