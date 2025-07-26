package interface_adapter.gpt;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.Ina.GPTService;

import java.io.IOException;

public class OpenAIAPIAdapter implements GPTService {
    private static final String API_KEY = System.getProperty("OPENAI_API_KEY");
    private static final String ENDPOINT = System.getProperty("OPENAI_API_KEY");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String generateFeedback(String prompt) {

        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", prompt);

        JSONObject body = new JSONObject()
                .put("model", "gpt-4o-mini")
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
            assert response.body() != null;
            return new JSONObject(response.body().string()) // TODO: May produce NullPointerException
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
