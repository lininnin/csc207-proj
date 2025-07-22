package data_access;

import entity.FeedbackEntry;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.repository.FeedbackRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FeedbackRepositoryImpl implements FeedbackRepository {
    private static final String file_path = "feedback_cache.json";
    //read directly from file
    /**
     * @param entry
     */
    @Override
    public void save(FeedbackEntry entry) {
        try {
            Path path = Paths.get(file_path);
            JSONObject root;
            if (Files.exists(path) && Files.size(path) > 0) {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            root = new JSONObject(new JSONTokener(content));
            } else {
            root = new JSONObject();
            }

            JSONObject obj = new JSONObject();
            obj.put("aiAnalysis", entry.getAiAnalysis());
            obj.put("Recommendations", entry.getRecommendations());
            obj.put("correlationData", entry.getCorrelationData());

            root.put(entry.getDate().toString(), obj);
            Files.writeString(path, root.toString(2), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Feedback Entry, please retry", e);
        }
    }

    /**
     * Load the entry saved on date from file.
     * @param date
     * @return the FeedbackEntry logged on date.
     */
    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        try {
            Path path = Paths.get(file_path);
            if (!Files.exists(path) || Files.size(path) == 0) {
                return null; // TODO: What to return?
            }
            String content = Files.readString(path, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(new JSONTokener(content));
            String key = date.toString();
            if (!root.has(key)) {
                return null;
            }
            JSONObject obj = root.getJSONObject(key);
            return new FeedbackEntry(
                    date,
                    obj.optString("aiAnalysis"),
                    obj.optString("recommendation"),
                    obj.optString("correlationData")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FeedbackEntry", e);
        }
    }
}