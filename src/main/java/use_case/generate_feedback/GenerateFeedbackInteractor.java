package use_case.generate_feedback;

import entity.Angela.DailyLog;
import entity.Ina.FeedbackEntry;
import interface_adapter.gpt.BayesCorrelationPromptBuilder;
import interface_adapter.gpt.GeneralAnalysisPromptBuilder;
import interface_adapter.gpt.RecommendationPromptBuilder;
import org.json.JSONObject;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;
import use_case.Ina.GPTService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Generates the weekly feedback entry. The scheduler triggers this use‑case **only on Mondays**,
 * so we no longer perform an explicit Day‑of‑Week check here—if the caller invokes it, generation proceeds.
 *
 * Workflow
 *  1. Determine this week’s Monday and see if it is already cached → return if yes.
 *  2. Load the last 7 days of DailyLogs (last Mon‑Sun inclusive).
 *  3. Call GPT 3 times (general analysis JSON → bayesian correlation JSON → recommendations).
 *  4. Persist and return a new FeedbackEntry.
 */
public class GenerateFeedbackInteractor implements GenerateFeedbackInputBoundary {

    private final DailyLogRepository dailyRepo;
    private final FeedbackRepository feedbackRepo;
    private final GPTService gpt;
    private final GenerateFeedbackOutputBoundary outputBoundary;

    public GenerateFeedbackInteractor(DailyLogRepository dailyRepo,
                                      FeedbackRepository feedbackRepo,
                                      GPTService gpt,
                                      GenerateFeedbackOutputBoundary outputBoundary) {
        this.dailyRepo = dailyRepo;
        this.feedbackRepo = feedbackRepo;
        this.gpt = gpt;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GenerateFeedbackInputData inputData) {
        try {
            LocalDate today  = LocalDate.now();
            LocalDate monday = today.with(DayOfWeek.MONDAY);

            FeedbackEntry cached = feedbackRepo.loadByDate(monday);
            if (cached != null) {
                outputBoundary.present(new GenerateFeedbackOutputData(cached));
                return;
            }

            LocalDate from = monday.minusWeeks(1);
            LocalDate to   = monday.minusDays(1);
            List<DailyLog> weekLogs = dailyRepo.loadBetween(from, to);

            String promptAnalysis  = GeneralAnalysisPromptBuilder.buildPromptFromWeeksLogs(weekLogs);
            String analysisJsonStr = gpt.callGeneralAnalysis(promptAnalysis);
            JSONObject analysisJson = new JSONObject(analysisJsonStr);
            String analysisText = analysisJson.optString("analysis", "(no analysis)");
            String extraNotes   = analysisJson.optString("extra_notes", "");

            String promptCorr   = BayesCorrelationPromptBuilder.buildPrompt(weekLogs);
            String correlationJson = gpt.callCorrelationBayes(promptCorr);

            String promptRec = RecommendationPromptBuilder.buildPrompt(analysisJsonStr);
            String recText   = gpt.callRecommendation(promptRec);

            String combinedAnalysis = extraNotes.isBlank()
                    ? analysisText
                    : analysisText + "\n\nNotes: " + extraNotes;

            FeedbackEntry entry = new FeedbackEntry(
                    monday,
                    combinedAnalysis,
                    recText,
                    correlationJson
            );
            feedbackRepo.save(entry);

            outputBoundary.present(new GenerateFeedbackOutputData(entry));
        } catch (IOException e) {
            // You can expand OutputBoundary for error reporting if you want.
            throw new RuntimeException(e);
        }
    }
}
