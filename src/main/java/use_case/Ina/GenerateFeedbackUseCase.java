package use_case.Ina;

import entity.Angela.DailyLog;
import entity.Ina.FeedbackEntry;
import interface_adapter.gpt.PromptBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static interface_adapter.gpt.DefaultPromptProvider.DEFAULT_PROMPT;

public class GenerateFeedbackUseCase {
    private final GPTService gptService;
    private final FeedbackRepository feedbackRepo;

    public GenerateFeedbackUseCase(GPTService gptService, FeedbackRepository feedbackRepo) {
        this.gptService = gptService;
        this.feedbackRepo = feedbackRepo;
        // A feedback is generated every Sunday/Monday
    }

    /**
     * Generate this weeks feedback
     * @return this week's generated feedback entry
     */
    public FeedbackEntry generateFeedback(List<DailyLog> logs) throws IOException {

            // If feedback already generated today,
            FeedbackEntry weekFeedback = feedbackRepo.loadByDate(LocalDate.now());
            if (weekFeedback != null) {
                return weekFeedback;
            }

        // Build prompt
        String prompt = PromptBuilder.buildPromptFromWeeklyLog();
        // Call GPT
        String aiAnalysis = gptService.generateFeedback(prompt);

        //

        FeedbackEntry weekEntry = new FeedbackEntry(LocalDate.now(), aiAnalysis, correlation, recommendations);

        feedbackRepo.save(weekEntry);

        return weekEntry;
    }
}
