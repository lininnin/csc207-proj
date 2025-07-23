package use_case.Ina;

import entity.Angela.DailyLog;
import entity.Ina.FeedbackEntry;
import interface_adapter.gpt.PromptBuilder;

import java.io.IOException;
import java.time.LocalDate;

public class GenerateFeedbackUseCase {
    private final GPTService gptService;
    private final FeedbackRepository feedbackRepo;

    public GenerateFeedbackUseCase(GPTService gptService, FeedbackRepository feedbackRepo) {
        this.gptService = gptService;
        this.feedbackRepo = feedbackRepo;
    }

    /**
     * Generate today's feedback
     *
     * @return today's generated feedback entry
     */
    public FeedbackEntry generateFeedback(DailyLog log) throws IOException {
        LocalDate date = log.getDate();

        // If feedback already generated today,
        FeedbackEntry todayFeedback = feedbackRepo.loadByDate(date);
        if (todayFeedback != null) {
            return todayFeedback;
        }

        // Build prompt
        String prompt = PromptBuilder.buildPromptFromDailyLog(log);
        // Call GPT
        String aiAnalysis = gptService.generateFeedback(prompt);

        //

        FeedbackEntry todayEntry = new FeedbackEntry(date, aiAnalysis); // TODO: How should we get the other 2?

        feedbackRepo.save(todayEntry);
        return todayEntry;
    }
}
