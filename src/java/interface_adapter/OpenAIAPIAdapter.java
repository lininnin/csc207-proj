package interface_adapter;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class OpenAIAPIAdapter implements GPTService{
    private static final String API_KEY = ""; // to be replaced by actual apikey
    private static final String endpoint = ""; // to be replaced by actual endpt
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
        // so how diversed would we want the response to be? consider a stable advice?

        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer" + API_KEY)
                .post(RequestBody.create((body.toString()), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected content" + response);
            }
            return new JSONObject(response.body().string())
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }



    }

}
