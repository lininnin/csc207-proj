package data_access;

import entity.FeedbackEntry;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.FeedbackRepository;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FeedbackRepositoryImpl implements FeedbackRepository {
    private static final String file_path = "feedback.json";
    /**
     * @param entry
     */
    @Override
    public void save(FeedbackEntry entry) {
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

    }

    /**
     * @param date
     * @return the FeedbackEntry logged on date.
     */
    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        return null;
    }
}
