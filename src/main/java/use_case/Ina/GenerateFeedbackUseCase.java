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

    public GenerateFeedbackUseCase(GPTService gptService, FeedbackRepository feedbackRepo) {
        this.gptService = gptService;
        this.feedbackRepo = feedbackRepo;
    }

    /**
     * Generate this week's feedback
     * @return this week's generated feedback entry
     */
    public FeedbackEntry generateFeedback(List<DailyLog> log) throws IOException {
        LocalDate date = LocalDate.now();

        // If feedback already generated today,
        FeedbackEntry todayFeedback = feedbackRepo.loadByDate(date);
        if (todayFeedback != null) {
            return todayFeedback;
        }

        // Build prompt
        String prompt = PromptBuilder.buildPromptFromWeeksLogs(log);
        // Call GPT
        String aiAnalysis = gptService.generateFeedback(prompt);

        //

        FeedbackEntry todayEntry = new FeedbackEntry(date, aiAnalysis, recommendation, ); // TODO: How should we get the other 2?

        feedbackRepo.save(todayEntry);
        return todayEntry;
    }
}
