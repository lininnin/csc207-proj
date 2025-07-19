package integration;

import fixtures.*;
import use_case.*;
import interface_adapter.gpt.OpenAIAPIAdapter;
import entity.FeedbackEntry;

import java.time.LocalDate;

public class GPTIntegrationSmokeTest {
    public static void main(String[] args) throws Exception {
        GPTService gpt = new OpenAIAPIAdapter();               // real adapter
        FeedbackRepository repo = new StubFeedbackRepository();// in-memory
        var corr = new StubAnalyzeCorrUseCase();
        var useCase = new GenerateFeedbackUseCase(gpt, repo,
                log -> 0.0  // or adapt if you made an interface
        );
        var log = new StubDailyLog(LocalDate.now()); // adapt when real DailyLog available

        FeedbackEntry entry = useCase.generateFeedback((entity.DailyLog)(Object) log);
        System.out.println(entry);
    }
}
