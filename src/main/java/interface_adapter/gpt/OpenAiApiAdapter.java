package interface_adapter.gpt;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import constants.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.generate_feedback.GptService;

// style checked
public class OpenAiApiAdapter implements GptService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String MODEL = "gpt-4o-mini";
    private static final String ENDPOINT = System.getProperty(
            "OPENAI_API_BASE_URL",
            "https://api.openai.com/v1/chat/completions");

    private final OkHttpClient client;
    private final String apiKey;

    public OpenAiApiAdapter() {
        this.apiKey = Objects.requireNonNull(System.getenv("OPENAI_API_KEY"), "OPENAI_API_KEY env variable not set.");

        this.client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(Constants.THIRTY))
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
        final JSONObject body = new JSONObject()
                .put("model", MODEL)
                .put("temperature", Constants.HALF)
                .put("messages", new JSONArray()
                        .put(new JSONObject().put("role", "user").put("content", userPrompt)));

        final Request req = new Request.Builder()
                .url(ENDPOINT)
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(body.toString(), JSON))
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException("GPT API error " + resp.code() + ": " + resp.body().string());
            }
            final String json = Objects.requireNonNull(resp.body()).string();
            final JSONArray choices = new JSONObject(json).getJSONArray("choices");
            return choices.getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        }
    }

}
