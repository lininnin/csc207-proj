package interface_adapter.generate_feedback;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import entity.Alex.DailyEventLog.DailyEventLogInterf;
import entity.Alex.DailyWellnessLog.DailyWellnessLogInterf;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Angela.DailyLog;
import entity.Angela.DailyTaskSummary;
import entity.Angela.Task.Task;
import use_case.generate_feedback.GptPrompt;

public class GptPromptAdapter implements GptPrompt {
    /**
     * Build a prompt asking GPT to perform a general wellness/productivity analysis based on listed weekly information.
     * @param logs daily logs for the past week (last monday to sunday)
     * @return the prompt string for gpt analysis
     */
    @Override
    public String buildAnalysis(List<DailyLog> logs) {
        // idea: extract info: completed tasks, stress lvl, moods, overdues, etc
        // buildPrompt coherent natural language prompt

        final StringBuilder prompt = new StringBuilder();

        prompt.append("You are an analyst and coach. "
                      + "Analyse the 7-day report below, DO NOT ANALYZE ANY OTHER DAYS OUTSIDE OF THE 7 DAYS"
                      + "summarize productivity patterns trends, correlations between amount of completed tasks, "
                      + "average wellness changes: \n ")
                .append("Analyze which scheduled tasks are completed more "
                        + "and how that may relate to productivity/wellness/mood. \n")
                .append("Analyze how number of events scheduled could affect productivity. \n")
                .append("Only give one sentence discussing missing data across the week if there are. "
                        + "Must focus on productivity trend and wellness")
                .append("Rules for missing data: If any day's data (tasks, wellness, or events) is missing or partial,"
                        + " explicitly flag it as MISSING.\n")
                .append("Discuss how that gap could relate to productivity or wellness trends seen on other days, "
                        + "but clearly mark such points as assumptions or possibilities "
                        + "(e.g., 'it is possible', 'may indicate'). Do NOT invent exact numbers.\n")
                .append("Focus on trends, correlations, and data gaps across different days. "
                        + "Analyze trends across remaining days if there is missing data. NO advice verbs there.\n")
                .append("If no data is present throughout the week, write "
                        + "\"No Data is present for analysis this week.\"");

        for (DailyLog log: logs) {
            prompt.append("=== Date: ").append(log.getDate()).append(" ===\n");

            // ------- Task summary ------
            extractTaskSummaries(log, prompt);

            // ------Wellness logs ------
            extractWellnessLog(log, prompt);

            // ------ Event logs ------
            extractEvents(log, prompt);

            //
        }
        prompt.append("WEEK DATA END.");

        prompt.append("""
OUTPUT REQUIREMENTS:
Return STRICT JSON only, no markdown.  Use this schema exactly:

{
  "analysis":    string,   // trends & correlations, NO advice
  "extra_notes": string    // notes on missing / partial data ("" if none)
}
            """);

        return prompt.toString();
    }

    /**
     * Build prompt for bayesian regression analysis from daily log of last 7 days.
     * @param logs daily log objects from the last 7 days
     * @return prompt string
     */
    @Override
    public String buildCorrelation(List<DailyLog> logs) {
        final StringBuilder sb = new StringBuilder();

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
                .append("If there is missing days then focus on analyzing the rest of the days.\n")
                .append("Return STRICT JSON only, no extra keys. \n\n")

                .append("INPUT:\n")
                .append(toWeekVectorJson(logs))
                .append("\n\n")

                .append("OUTPUT JSON SCHEMA:\n")
                .append("{\n")
                .append("  \"effect_summary\": [\n")
                .append("    {\"variable\":\"Stress\",\"direction\":\"Positive|Negative|None\","
                        + "\"confidence\":0.0‑1.0},\n")
                .append("    {\"variable\":\"Energy\", ...},\n")
                .append("    {\"variable\":\"Fatigue\", ...}\n")
                .append("  ],\n")
                .append("}\n");

        return sb.toString();
    }

    /**
     * Build a prompt that returns recommendations based on the user's weekly productivity analysis.
     * @param analysisJson JSON string returned by buildAnalysis call
     * @return prompt string for GPT
     */
    @Override
    public String buildRecommendation(String analysisJson) {
        final JSONObject obj = new JSONObject(analysisJson);
        final String analysisTxt = obj.optString("analysis", "(no analysis)");
        final String dataGapsTxt = obj.optString("extra_notes", "None");

        return """
You are a productivity & wellness coach.  Provide concise, concrete advice only.

Below is this week's analysis%s
Write 3–5 imperative sentences of recommendations for next week.
Return in form of numerical list.

ANALYSIS:
%s

OUTPUT:
Plain text only – 3 to 5 sentences.
""".formatted(
                dataGapsTxt.isBlank() ? "" : " (and a note about data gaps)",
                analysisTxt.trim());
    }


    // Helper methods
    private static String toWeekVectorJson(List<DailyLog> logs) {
        return logs.stream().map(dailyLog -> {
            final double completion = dailyLog.getDailyTaskSummary() == null
                    ? Double.NaN : dailyLog.getDailyTaskSummary().getCompletionRate();

            // average wellness levels for the day
            final double stress = avg(dailyLog, logEntry -> logEntry.getStressLevel().getValue());
            final double energy = avg(dailyLog, logEntry -> logEntry.getEnergyLevel().getValue());
            final double fatigue = avg(dailyLog, logEntry -> logEntry.getFatigueLevel().getValue());

            return String.format("{\"date\":\"%s\",\"completion_rate\":%.3f,"
                                 + "\"Stress\":%.2f,\"Energy\":%.2f,\"Fatigue\":%.2f}",
                    dailyLog.getDate(), completion, stress, energy, fatigue);
        }).collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

    private static double avg(DailyLog dailyLog, java.util.function.ToIntFunction<WellnessLogEntryInterf> map) {
        if (dailyLog.getDailyWellnessLog() == null || dailyLog.getDailyWellnessLog().getEntries().isEmpty()) {
            return Double.NaN;
        }
        return dailyLog.getDailyWellnessLog().getEntries().stream()
                .mapToInt(map).average().orElse(Double.NaN);
    }

    private static void extractEvents(DailyLog log, StringBuilder prompt) {
        final DailyEventLogInterf events = log.getDailyEventLog();
        final int numEvents = events.getActualEvents().size();
        prompt.append("Events amount: ").append(numEvents);
        prompt.append(", Events scheduled: ");
        if (events != null && events.getActualEvents() != null && !events.getActualEvents().isEmpty()) {
            for (EventInterf event : events.getActualEvents()) {
                prompt.append("- ").append(event.getInfo().getName()).append('\n');
            }
        }
        else {
            prompt.append("No Events data for this date.");
        }
        prompt.append('\n');
    }

    private static void extractTaskSummaries(DailyLog log, StringBuilder prompt) {
        prompt.append("Task summary: \n");
        final DailyTaskSummary summary = log.getDailyTaskSummary();
        if (summary != null) {
            final int scheduled = summary.getScheduledTasks().size();
            final List<Task> scheduledTasks = summary.getScheduledTasks();
            final int completed = summary.getCompletedTasks().size();
            final List<Task> completedTasks = summary.getCompletedTasks();
            final int overdue = scheduled - completed;

            prompt.append("Tasks Scheduled: ").append(scheduled)
                    .append(", Scheduled tasks: ").append(scheduledTasks)
                    .append(", Completed amount: ").append(completed)
                    .append(", Completed task list:").append(completedTasks)
                    .append(", Overdue: ").append(overdue)
                    .append("Completion rate:").append(summary.getCompletionRate())
                    .append("\n");

            if (!summary.getCategoryBreakdown().isEmpty() && summary.getCategoryBreakdown() != null) {
                final String categories = summary.getCategoryBreakdown().entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(","));
                prompt.append("Category breakdown: ").append(categories).append('\n');
            }
            else {
                prompt.append("No task data available for this date.");
            }
        }
    }

    private static void extractWellnessLog(DailyLog log, StringBuilder prompt) {
        prompt.append("Wellness Log: \n");
        final DailyWellnessLogInterf wellnessLog = log.getDailyWellnessLog();
        if (wellnessLog != null && wellnessLog.getEntries() != null && !wellnessLog.getEntries().isEmpty()) {
            for (WellnessLogEntryInterf entry : wellnessLog.getEntries()) {
                prompt.append("---")
                        .append("Stress level: ").append(entry.getStressLevel()).append(",")
                        .append("Energy level: ").append(entry.getEnergyLevel()).append(",")
                        .append("Fatigue level: ").append(entry.getFatigueLevel()).append(",")
                        .append("Mood: ").append(entry.getMoodLabel().getName())
                        .append(", analyze how this mood may influence the above wellness levels and completion rate.");
                if (entry.getUserNote() != null && !entry.getUserNote().isBlank()) {
                    prompt.append("Note: ").append(entry.getUserNote());
                }
                prompt.append('\n');
            }
        }
        else {
            prompt.append("No wellness data on record for this date.");
        }
    }
}
