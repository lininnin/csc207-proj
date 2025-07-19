package use_case;

import entity.DailyLog;
import entity.FeedbackEntry;
import interface_adapter.gpt.PromptBuilder;

import java.io.IOException;

public class GenerateFeedbackUseCase {
    private final GPTService gptService;
    private final FeedbackRepository feedbackRepo;

    public GenerateFeedbackUseCase(GPTService gptService, FeedbackRepository feedbackRepo) {
        this.gptService = gptService;
        this.feedbackRepo = feedbackRepo;
    }

    public void generateFeedback(DailyLog log) throws IOException {
        String prompt = PromptBuilder.buildPromptFromDailyLog(log);
        String feedbackText = gptService.generateFeedback(prompt);

        FeedbackEntry entry = new FeedbackEntry(
                log,
                feedbackText,
                "See analysis above",
                "Correlation details..."
        );

        feedbackRepo.save(entry);
    }
}
