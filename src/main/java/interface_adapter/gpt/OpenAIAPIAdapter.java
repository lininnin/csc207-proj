package interface_adapter.gpt;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.GPTService;

import java.io.IOException;

public class OpenAIAPIAdapter implements GPTService {
    private static final String API_KEY = System.getProperty("OPENAI_API_KEY");
    private static final String ENDPOINT = System.getProperty("OPENAI_API_BASE_URL");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String generateFeedback(String prompt) {

        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", prompt);

        JSONObject body = new JSONObject()
                .put("model", "gpt-4o")
                .put("messages", new JSONArray().put(message))
                .put("temperature", 0.7);
        // so how diverse would we want the response to be? consider a stable advice?

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .header("Authorization", "Bearer" + API_KEY)
                .post(RequestBody.create(
                        body.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API error:" + response.code() + " " + response.message());
            }
            return new JSONObject(response.body().string()) // TODO: May produce NullPointerException
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }


    }

    /**
     * Main method for verification of environment variable setup.
     */
    public static void main(String[] args) {
        System.out.println("=== API Configuration Check ===");
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.out.println("[WARN] OPENAI_API_KEY is not set.");
        } else {
            System.out.println("[OK] OPENAI_API_KEY is set.");
        }
        System.out.println("Endpoint: " + ENDPOINT);
        System.out.println("To set environment variables, use your shell or IDE Run Configuration.");
    }

}
