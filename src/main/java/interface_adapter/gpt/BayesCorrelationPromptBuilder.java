package interface_adapter.gpt;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Angela.DailyLog;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a Bayesian‑correlation prompt for the last 7 days.
 * The LLM should return JSON
 * Expected JSON response:
 *  {
 *    "effect_summary":[
 *      {"variable":"stress","direction":"positive|negative|none","confidence":0.0‑1.0},
 *      { ... energy ... },
 *      { ... fatigue ... }
 *    ],
 *    "notes":"one short sentence"
*    }
 */
public final class BayesCorrelationPromptBuilder {

    private BayesCorrelationPromptBuilder() {}

    /**
     * @param weekLogs daily log objects from the last 7 days
     * @return prompt string
     */
    public static String buildPrompt(List<DailyLog> weekLogs) {
        StringBuilder sb = new StringBuilder();

        sb.append("SYSTEM:\n")
                .append("You are a statistician performing a simple Bayesian regression\n")
                .append("to relate wellness metrics to task‑completion rate.\n\n")

                .append("USER:\n")
                .append("For each of the last 7 days you get:\n")
                .append("  date, completion_rate (0‑1), avg_stress, avg_energy, avg_fatigue.\n")
                .append("Assume Normal(0,1) priors on coefficients and independent variables.\n")
                .append("Task:\n")
                .append(" 1. Compute the posterior mean *direction* (+ / – / ~0) for each variable.\n")
                .append(" 2. Give a 0‑1 confidence score that the sign is correct.\n")
                .append(" 3. Add one‑sentence methodological note.\n")
                .append("If there is missing days then focus on analyzing the rest of the days.\n")
                .append("Return STRICT JSON only, no extra keys. \n\n")

                .append("INPUT:\n")
                .append(toWeekVectorJson(weekLogs))
                .append("\n\n")

                .append("OUTPUT JSON SCHEMA:\n")
                .append("{\n")
                .append("  \"effect_summary\": [\n")
                .append("    {\"variable\":\"Stress\",\"direction\":\"Positive|Negative|None\",\"confidence\":0.0‑1.0},\n")
                .append("    {\"variable\":\"Energy\", ...},\n")
                .append("    {\"variable\":\"Fatigue\", ...}\n")
                .append("  ],\n")
                .append("  \"notes\": \"one short sentence, if data is missing specify which data, including metrics\"\n")
                .append("}\n");

        return sb.toString();
    }

    private static String toWeekVectorJson(List<DailyLog> logs) {
        return logs.stream().map(dl -> {
            double completion = dl.getDailyTaskSummary() == null
                    ? Double.NaN : dl.getDailyTaskSummary().getCompletionRate();

            // average wellness levels for the day
            double stress = avg(dl, w -> w.getStressLevel().getValue());
            double energy = avg(dl, w -> w.getEnergyLevel().getValue());
            double fatigue = avg(dl, w -> w.getFatigueLevel().getValue());

            return String.format("{\"date\":\"%s\",\"completion_rate\":%.3f," +
                            "\"Stress\":%.2f,\"Energy\":%.2f,\"Fatigue\":%.2f}",
                    dl.getDate(), completion, stress, energy, fatigue);
        }).collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

    private static double avg(DailyLog dl, java.util.function.ToIntFunction<WellnessLogEntryInterf> f) {
        if (dl.getDailyWellnessLog() == null || dl.getDailyWellnessLog().getEntries().isEmpty())
            return Double.NaN;
        return dl.getDailyWellnessLog().getEntries().stream()
                .mapToInt(f).average().orElse(Double.NaN);
    }
}
