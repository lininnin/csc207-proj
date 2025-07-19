package fixtures;

import entity.DailyLog;
import entity.FeedbackEntry;
import interface_adapter.gpt.PromptBuilder;
import use_case.FeedbackRepository;
import use_case.GPTService;

import java.io.IOException;
import java.time.LocalDate;

import static fixtures.DefaultPromptProvider.DEFAULT_PROMPT;

public class StubGenerateFeedback {
    private final GPTService gptService;
    private final FeedbackRepository feedbackRepo;

    public StubGenerateFeedback(GPTService gptService, FeedbackRepository feedbackRepo) {
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

        // Call GPT
        String aiAnalysis = gptService.generateFeedback(DEFAULT_PROMPT);

        //

        FeedbackEntry todayEntry = new FeedbackEntry(date, aiAnalysis, recommendations, computeTaskStressCorr); // TODO: How should we get the other 2?

        feedbackRepo.save(todayEntry);
        return todayEntry;
    }
}
