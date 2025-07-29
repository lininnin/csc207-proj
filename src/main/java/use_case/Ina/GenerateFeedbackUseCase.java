package use_case.Ina;

import entity.Angela.DailyLog;
import entity.Ina.FeedbackEntry;
import interface_adapter.gpt.BayesCorrelationPromptBuilder;
import interface_adapter.gpt.GeneralAnalysisPromptBuilder;
import interface_adapter.gpt.RecommendationPromptBuilder;
import org.json.JSONObject;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;

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
 *  2. Load the last 7 days of DailyLogs (Mon‑Sun inclusive).
 *  3. Call GPT 3× (general analysis JSON → bayesian correlation JSON → recommendations).
 *  4. Persist and return a new FeedbackEntry.
 */
public class GenerateFeedbackUseCase {

    private final DailyLogRepository dailyRepo;
    private final FeedbackRepository feedbackRepo;
    private final GPTService gpt;

    public GenerateFeedbackUseCase(DailyLogRepository dailyRepo,
                                   FeedbackRepository feedbackRepo,
                                   GPTService gpt) {
        this.dailyRepo    = dailyRepo;
        this.feedbackRepo = feedbackRepo;
        this.gpt          = gpt;
    }

    /**
     * Creates—or returns cached—the feedback entry for the week containing *today*.
     */
    public FeedbackEntry execute() throws IOException {
        LocalDate today  = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);   // key date for the week

        // 1. Cached already?
        FeedbackEntry cached = feedbackRepo.loadByDate(monday);
        if (cached != null) return cached;

        // 2. Determine last week's window (previous Monday -> Sunday)
        LocalDate from = monday.minusWeeks(1);             // last week's Monday
        LocalDate to   = monday.minusDays(1);              // yesterday (Sunday)

        List<DailyLog> weekLogs = dailyRepo.loadBetween(from, to);

        // 3a. General analysis (JSON). General analysis (JSON)
        String promptAnalysis  = GeneralAnalysisPromptBuilder.buildPromptFromWeeksLogs(weekLogs);
        String analysisJsonStr = gpt.callGeneralAnalysis(promptAnalysis);
        JSONObject analysisJson = new JSONObject(analysisJsonStr);
        String analysisText = analysisJson.optString("analysis", "(no analysis)");
        String extraNotes   = analysisJson.optString("extra_notes", "");

        // 3b. Bayesian correlation (JSON)
        String promptCorr   = BayesCorrelationPromptBuilder.buildPrompt(weekLogs);
        String correlationJson = gpt.callCorrelationBayes(promptCorr);

        // 3c. Recommendations
        String promptRec = RecommendationPromptBuilder.buildPrompt(analysisJsonStr);
        String recText   = gpt.callRecommendation(promptRec);

        // 4. Combine & save
        String combinedAnalysis = extraNotes.isBlank()
                ? analysisText
                : analysisText + "\n\nNotes: " + extraNotes;

        FeedbackEntry entry = new FeedbackEntry(
                monday,               // stored under the Monday of that week
                combinedAnalysis,
                recText,
                correlationJson
        );
        feedbackRepo.save(entry);
        return entry;
    }
}
