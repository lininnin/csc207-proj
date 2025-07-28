package use_case.Ina;

import entity.Angela.DailyLog;
import entity.Ina.FeedbackEntry;
import interface_adapter.gpt.PromptBuilder;
import use_case.repository.FeedbackRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class GenerateFeedbackUseCase {
    private final GPTService gptService;
    private final FeedbackRepository feedbackRepo;

    public FeedbackEntry generateFeedback(List<DailyLog> logs) throws IOException {
        LocalDate date = LocalDate.now();

        FeedbackEntry existing = feedbackRepo.loadByDate(date);
        if (existing != null) return existing;

        String prompt = PromptBuilder.buildPromptFromWeeksLogs(logs);
        String raw = gptService.generateFeedback(prompt);

        String analysis = null;
        String recommendations = null;
        try {
            org.json.JSONObject o = new org.json.JSONObject(raw);
            if (o.has("analysis") && !o.isNull("analysis")) {
                analysis = o.getString("analysis");
            }
            if (o.has("recommendations") && !o.isNull("recommendations")) {
                recommendations = o.getString("recommendations");
            }
        } catch (org.json.JSONException ignore) { /* leave null */ }

        // Nothing parsed → don't create/save entry
        if (analysis == null && recommendations == null) {
            // optionally log raw
            return null; // or throw if you prefer
        }

        FeedbackEntry weekEntry = new FeedbackEntry(date, analysis, recommendations);
        //TODO: deal with correlation.?
        feedbackRepo.save(weekEntry);
        return weekEntry;
    }

    }
}
