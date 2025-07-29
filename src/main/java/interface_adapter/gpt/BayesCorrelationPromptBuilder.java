package interface_adapter.gpt;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
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
 */
public final class BayesCorrelationPromptBuilder {

    private BayesCorrelationPromptBuilder() {}

    /**
     * @param weekLogs exactly 7 DailyLog objects (newest last is fine)
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
                .append("Return STRICT JSON only, no extra keys.\n\n")

                .append("INPUT:\n")
                .append(toWeekVectorJson(weekLogs))
                .append("\n\n")

                .append("OUTPUT JSON SCHEMA:\n")
                .append("{\n")
                .append("  \"effect_summary\": [\n")
                .append("    {\"variable\":\"stress\",\"direction\":\"positive|negative|none\",\"confidence\":0.0‑1.0},\n")
                .append("    {\"variable\":\"energy\", ...},\n")
                .append("    {\"variable\":\"fatigue\", ...}\n")
                .append("  ],\n")
                .append("  \"notes\": \"one short sentence\"\n")
                .append("}\n");

        return sb.toString();
    }

    private static String toWeekVectorJson(List<DailyLog> logs) {
        return logs.stream().map(dl -> {
            double completion = dl.getDailyTaskSummary() == null
                    ? Double.NaN : dl.getDailyTaskSummary().getCompletionRate();

            // average wellness levels for the day
            double stress = avg(dl, WellnessLogEntry::getStressLevel);
            double energy = avg(dl, WellnessLogEntry::getEnergyLevel);
            double fatigue = avg(dl, WellnessLogEntry::getFatigueLevel);

            return String.format("{\"date\":\"%s\",\"completion_rate\":%.3f," +
                            "\"stress\":%.2f,\"energy\":%.2f,\"fatigue\":%.2f}",
                    dl.getDate(), completion, stress, energy, fatigue);
        }).collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

    private static double avg(DailyLog dl, java.util.function.ToIntFunction<WellnessLogEntry> f) {
        if (dl.getDailyWellnessLog() == null || dl.getDailyWellnessLog().getEntries().isEmpty())
            return Double.NaN;
        return dl.getDailyWellnessLog().getEntries().stream()
                .mapToInt(f).average().orElse(Double.NaN);
    }
}
