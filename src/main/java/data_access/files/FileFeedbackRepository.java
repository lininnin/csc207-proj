package data_access.files;

import entity.Ina.FeedbackEntry;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.repository.FeedbackRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileFeedbackRepository implements FeedbackRepository {
    private static final String file_path = "feedback_cache.json";

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
            obj.put("correlationData", entry.getCorrelationData());
            obj.put("Recommendations", entry.getRecommendations()); // Consistent key

            root.put(entry.getDate().toString(), obj);
            Files.writeString(path, root.toString(2), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Feedback Entry, please retry", e);
        }
    }

    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        try {
            Path path = Paths.get(file_path);
            if (!Files.exists(path) || Files.size(path) == 0) {
                return null;
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
                    obj.optString("aiAnalysis", null),
                    obj.optString("correlationData", null),
                    obj.optString("Recommendations", null)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FeedbackEntry", e);
        }
    }

    @Override
    public List<FeedbackEntry> loadAll() {
        List<FeedbackEntry> list = new ArrayList<>();
        Path path = Paths.get(file_path);

        if (!Files.exists(path)) return list;

        try {
            if (Files.size(path) == 0) return list;

            String content = Files.readString(path, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(new JSONTokener(content));

            for (String key : root.keySet()) {
                LocalDate date = LocalDate.parse(key);
                JSONObject obj = root.getJSONObject(key);
                list.add(new FeedbackEntry(
                        date,
                        obj.optString("aiAnalysis", null),
                        obj.optString("correlationData", null),
                        obj.optString("Recommendations", null)
                        ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load feedback entries", e);
        }
        return list;
    }
}
