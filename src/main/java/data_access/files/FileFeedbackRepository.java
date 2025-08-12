package data_access.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

import constants.Constants;
import entity.feedback_entry.FeedbackEntry;
import use_case.repository.FeedbackRepository;

public class FileFeedbackRepository implements FeedbackRepository {
    private static final String FILE_PATH = "feedback_cache.json";

    @Override
    public void save(FeedbackEntry entry) {
        try {
            final Path path = Paths.get(FILE_PATH);
            final JSONObject root;
            if (Files.exists(path) && Files.size(path) > 0) {
                final String content = Files.readString(path, StandardCharsets.UTF_8);
                root = new JSONObject(new JSONTokener(content));
            }
            else {
                root = new JSONObject();
            }

            final JSONObject obj = new JSONObject();
            obj.put(Constants.ANALYSIS, entry.getAiAnalysis());
            obj.put("correlationData", entry.getCorrelationData());
            obj.put(Constants.RECOMMENDATIONS, entry.getRecommendations());

            root.put(entry.getDate().toString(), obj);
            Files.writeString(path, root.toString(2), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to save Feedback Entry, please retry", exception);
        }
    }

    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        try {
            final Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path) || Files.size(path) == 0) {
                return null;
            }
            final String content = Files.readString(path, StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(new JSONTokener(content));
            final String key = date.toString();
            if (!root.has(key)) {
                return null;
            }
            final JSONObject obj = root.getJSONObject(key);
            return new FeedbackEntry(
                    date,
                    obj.optString(Constants.ANALYSIS, null),
                    obj.optString(Constants.CORRELATION, null),
                    obj.optString(Constants.RECOMMENDATIONS, null)
            );
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to load FeedbackEntry", exception);
        }
    }

    @Override
    public List<FeedbackEntry> loadAll() {
        final List<FeedbackEntry> list = new ArrayList<>();
        final Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return list;
        }

        try {
            if (Files.size(path) == 0) {
                return list;
            }

            final String content = Files.readString(path, StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(new JSONTokener(content));

            for (String key : root.keySet()) {
                final LocalDate date = LocalDate.parse(key);
                final JSONObject obj = root.getJSONObject(key);
                list.add(new FeedbackEntry(
                        date,
                        obj.optString(Constants.ANALYSIS, null),
                        obj.optString(Constants.CORRELATION, null),
                        obj.optString(Constants.RECOMMENDATIONS, null)
                        ));
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to load feedback entries", exception);
        }

        list.sort(Comparator.comparing(FeedbackEntry::getDate).reversed());
        return List.copyOf(list);
    }
}
