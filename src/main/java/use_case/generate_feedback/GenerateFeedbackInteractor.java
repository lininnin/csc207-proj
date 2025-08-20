package use_case.generate_feedback;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.json.JSONObject;

import entity.Angela.DailyLog;
import entity.feedback_entry.FeedbackEntryFactory;
import entity.feedback_entry.FeedbackEntryFactoryInterf;
import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.gpt.BayesCorrelationPromptBuilder;
import interface_adapter.gpt.GeneralAnalysisPromptBuilder;
import interface_adapter.gpt.RecommendationPromptBuilder;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;
/**
 * Generates the weekly feedback entry. The scheduler triggers this use‑case only during monday midnight,
 * so we no longer perform an explicit Day‑of‑Week check here—if the caller invokes it, generation proceeds.
 * Workflow
 *  1. Determine this week’s Monday and see if it is already cached → return if yes.
 *  2. Load the last 7 days of DailyLogs (last Mon‑Sun inclusive).
 *  3. Call GPT 3 times (general analysis JSON → bayesian correlation JSON → recommendations).
 *  4. Persist and return a new FeedbackEntry.
 */

public class GenerateFeedbackInteractor implements GenerateFeedbackInputBoundary {

    private final DailyLogRepository dailyRepo;
    private final FeedbackRepository feedbackRepo;
    private final GptService gpt;
    private final GenerateFeedbackOutputBoundary outputBoundary;

    public GenerateFeedbackInteractor(DailyLogRepository dailyRepo,
                                      FeedbackRepository feedbackRepo,
                                      GptService gpt,
                                      GenerateFeedbackOutputBoundary outputBoundary) {
        this.dailyRepo = dailyRepo;
        this.feedbackRepo = feedbackRepo;
        this.gpt = gpt;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GenerateFeedbackInputData inputData) {
        try {
            final LocalDate today = LocalDate.now();
            final LocalDate monday = today.with(DayOfWeek.MONDAY);

            final FeedbackEntryInterf cached = feedbackRepo.loadByDate(monday);
            if (cached != null) {
                outputBoundary.present(new GenerateFeedbackOutputData(cached));
                return;
            }

            final LocalDate from = monday.minusWeeks(1);
            final LocalDate to = monday.minusDays(1);
            final List<DailyLog> weekLogs = dailyRepo.loadBetween(from, to);

            final String promptAnalysis = GeneralAnalysisPromptBuilder.buildPromptFromWeeksLogs(weekLogs);
            final String analysisJsonStr = gpt.callGeneralAnalysis(promptAnalysis);
            final JSONObject analysisJson = new JSONObject(analysisJsonStr);

            // Analysis
            final String analysisText = analysisJson.optString("analysis", "(no analysis)");
            final String extraNotes = analysisJson.optString("extra_notes", "");
            // Correlation
            final String promptCorr = BayesCorrelationPromptBuilder.buildPrompt(weekLogs);
            final String correlationJson = gpt.callCorrelationBayes(promptCorr);
            // Recommendation
            final String promptRec = RecommendationPromptBuilder.buildPrompt(analysisJsonStr);
            final String recText = gpt.callRecommendation(promptRec);

            final String combinedAnalysis;
            if (extraNotes.isBlank()) {
                combinedAnalysis = analysisText;
            }
            else {
                combinedAnalysis = analysisText + "\n\nNotes: " + extraNotes;
            }

            final FeedbackEntryFactoryInterf factory = new FeedbackEntryFactory();

            final FeedbackEntryInterf entry = factory.create(monday, combinedAnalysis, correlationJson, recText);
            feedbackRepo.save(entry);

            outputBoundary.present(new GenerateFeedbackOutputData(entry));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
