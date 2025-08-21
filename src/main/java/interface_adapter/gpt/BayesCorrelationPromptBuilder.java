//package interface_adapter.gpt;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
//import entity.Angela.DailyLog;
//
///**
// * Builds a Bayesian‑correlation prompt for the last 7 days.
// * The LLM should return JSON
// * Expected JSON response:
// *  {
// *    "effect_summary":[
// *      {"variable":"stress","direction":"positive|negative|none","confidence":0.0‑1.0},
// *      { ... energy ... },
// *      { ... fatigue ... }
// *    ],
// *    "notes":"one short sentence"
//*    }
// */
//public final class BayesCorrelationPromptBuilder {
//
//    private BayesCorrelationPromptBuilder() {
//
//    }
//
//    /**
//     * Build prompt for bayesian regression analysis from daily log of last 7 days.
//     * @param weekLogs daily log objects from the last 7 days
//     * @return prompt string
//     */
//    public static String buildPrompt(List<DailyLog> weekLogs) {
//        final StringBuilder sb = new StringBuilder();
//
//        sb.append("SYSTEM:\n")
//                .append("You are a statistician performing a simple Bayesian regression\n")
//                .append("to relate wellness metrics to task‑completion rate.\n\n")
//
//                .append("USER:\n")
//                .append("For each of the last 7 days you get:\n")
//                .append("  date, completion_rate (0‑1), avg_stress, avg_energy, avg_fatigue.\n")
//                .append("Assume Normal(0,1) priors on coefficients and independent variables.\n")
//                .append("Task:\n")
//                .append(" 1. Compute the posterior mean *direction* (+ / – / ~0) for each variable.\n")
//                .append(" 2. Give a 0‑1 confidence score that the sign is correct.\n")
//                .append("If there is missing days then focus on analyzing the rest of the days.\n")
//                .append("Return STRICT JSON only, no extra keys. \n\n")
//
//                .append("INPUT:\n")
//                .append(toWeekVectorJson(weekLogs))
//                .append("\n\n")
//
//                .append("OUTPUT JSON SCHEMA:\n")
//                .append("{\n")
//                .append("  \"effect_summary\": [\n")
//                .append("    {\"variable\":\"Stress\",\"direction\":\"Positive|Negative|None\","
//                        + "\"confidence\":0.0‑1.0},\n")
//                .append("    {\"variable\":\"Energy\", ...},\n")
//                .append("    {\"variable\":\"Fatigue\", ...}\n")
//                .append("  ],\n")
//                .append("}\n");
//
//        return sb.toString();
//    }
//
//    private static String toWeekVectorJson(List<DailyLog> logs) {
//        return logs.stream().map(dailyLog -> {
//            final double completion = dailyLog.getDailyTaskSummary() == null
//                    ? Double.NaN : dailyLog.getDailyTaskSummary().getCompletionRate();
//
//            // average wellness levels for the day
//            final double stress = avg(dailyLog, logEntry -> logEntry.getStressLevel().getValue());
//            final double energy = avg(dailyLog, logEntry -> logEntry.getEnergyLevel().getValue());
//            final double fatigue = avg(dailyLog, logEntry -> logEntry.getFatigueLevel().getValue());
//
//            return String.format("{\"date\":\"%s\",\"completion_rate\":%.3f,"
//                            + "\"Stress\":%.2f,\"Energy\":%.2f,\"Fatigue\":%.2f}",
//                    dailyLog.getDate(), completion, stress, energy, fatigue);
//        }).collect(Collectors.joining(",\n", "[\n", "\n]"));
//    }
//
//    private static double avg(DailyLog dailyLog, java.util.function.ToIntFunction<WellnessLogEntryInterf> map) {
//        if (dailyLog.getDailyWellnessLog() == null || dailyLog.getDailyWellnessLog().getEntries().isEmpty()) {
//            return Double.NaN;
//        }
//        return dailyLog.getDailyWellnessLog().getEntries().stream()
//                .mapToInt(map).average().orElse(Double.NaN);
//    }
//}
