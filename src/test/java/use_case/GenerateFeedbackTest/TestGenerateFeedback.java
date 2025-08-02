package use_case.GenerateFeedbackTest;

import data_access.in_memory_repo.InMemoryDailyLogRepo;
import entity.Angela.DailyLog;

import interface_adapter.gpt.OpenAIAPIAdapter;
import use_case.generate_feedback.GenerateFeedbackInputData;
import use_case.generate_feedback.GenerateFeedbackInteractor;
import use_case.generate_feedback.GenerateFeedbackOutputBoundary;
import use_case.repository.*;
import entity.Ina.FeedbackEntry;

import java.time.LocalDate;

public class TestGenerateFeedback {
    public static void main(String[] args) {
        // 1. Fake logs for the week
        InMemoryDailyLogRepo dailyRepo = new InMemoryDailyLogRepo();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dailyRepo.save(new DailyLog(today.minusDays(i)));
        }

        // 2. Feedback repo
        InMemoryFeedbackRepository feedbackRepo = new InMemoryFeedbackRepository();

        // 3. GPT service
        OpenAIAPIAdapter adapter = new OpenAIAPIAdapter();

        // 4. OutputBoundary: print to console
        GenerateFeedbackOutputBoundary output = data -> {
            FeedbackEntry entry = data.getFeedbackEntry();
            System.out.println("Analysis: " + entry.getAiAnalysis());
            System.out.println("Correlation: " + entry.getCorrelationData());
            System.out.println("Recommendation: " + entry.getRecommendations());
        };

        // 5. Create and run interactor
        GenerateFeedbackInteractor interactor = new GenerateFeedbackInteractor(
                dailyRepo, feedbackRepo, adapter, output
        );
        interactor.execute(new GenerateFeedbackInputData());
    }
}
