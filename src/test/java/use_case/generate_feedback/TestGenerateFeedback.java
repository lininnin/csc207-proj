package use_case.generate_feedback;

import data_access.in_memory_repo.InMemoryDailyLogRepo;
import data_access.in_memory_repo.InMemoryFeedbackRepository;
import entity.Angela.DailyLog;

import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.generate_feedback.OpenAiApiAdapter;

import java.util.List;

public class TestGenerateFeedback {
    public static void main(String[] args) {
        // 1. Fake logs for the week
        InMemoryDailyLogRepo dailyRepo = new InMemoryDailyLogRepo();
        List<DailyLog> logs = DailyLogGenerator.generateFakeLogs();
        for (DailyLog log: logs) {
            dailyRepo.save(log);
        }

        // 2. Feedback repo
        InMemoryFeedbackRepository feedbackRepo = new InMemoryFeedbackRepository();

        // 3. GPT service
        OpenAiApiAdapter adapter = new OpenAiApiAdapter();

        // 4. OutputBoundary: print to console
        GenerateFeedbackOutputBoundary output = data -> {
            FeedbackEntryInterf entry = data.getFeedbackEntry();
            System.out.println("Analysis: " + entry.getAiAnalysis());
            System.out.println("Correlation: " + entry.getCorrelationData());
            System.out.println("Recommendation: " + entry.getRecommendations());
        };

        // 5. Create and run interactor
        GenerateFeedbackInteractor interactor = new GenerateFeedbackInteractor(
                dailyRepo, feedbackRepo, adapter, output
        );
        interactor.execute();
    }
}
