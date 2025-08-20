package interface_adapter.gpt;

import java.util.List;
import java.util.stream.Collectors;

import entity.Alex.DailyEventLog.DailyEventLogInterf;
import entity.Alex.DailyWellnessLog.DailyWellnessLogInterf;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Angela.DailyLog;
import entity.Angela.DailyTaskSummary;

/**
 * Output a JSON regarding users weekly productivity.
 * {
 *   "analysis": "…plain‑text paragraph…",
 *   "extra_notes": "…anything you want flagged (e.g., missing data)…"
 * }
 */
public class GeneralAnalysisPromptBuilder {
    /**
     * Build a prompt asking GPT to perform a general wellness/productivity analysis based on listed weekly information.
     * @param logs daily logs for the past week (last monday to sunday)
     * @return the prompt string for gpt analysis
     */
    public static String buildPromptFromWeeksLogs(List<DailyLog> logs) {
        // idea: extract info: completed tasks, stress lvl, moods, overdues, etc
        // build coherent natural language prompt

        final StringBuilder prompt = new StringBuilder();

        prompt.append("You are an analyst and coach. "
                        + "Analyse the 7-day report below, DO NOT ANALYZE ANY OTHER DAYS OUTSIDE OF THE 7 DAYS"
                        + "summarize productivity patterns trends, correlations between amount of completed tasks, "
                        + "average wellness changes: \n "
                        + "Only give one sentence discussing missing data across the week if there are. "
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
            prompt.append("Events: ");
            final DailyEventLogInterf events = log.getDailyEventLog();
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

    private static void extractTaskSummaries(DailyLog log, StringBuilder prompt) {
        prompt.append("Task summary: \n");
        final DailyTaskSummary summary = log.getDailyTaskSummary();
        if (summary != null) {
            final int scheduled = summary.getScheduledTasks().size();
            final int completed = summary.getCompletedTasks().size();
            final int overdue = scheduled - completed;

            prompt.append("Tasks Scheduled: ").append(scheduled)
                    .append(", Completed: ").append(completed)
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
                        .append("Energy level: ").append(entry.getStressLevel()).append(",")
                        .append("Fatigue level: ").append(entry.getStressLevel()).append(",")
                        .append("Mood: ").append(entry.getMoodLabel().getName())
                        .append(", analyze how this mood may influence the above wellness levels.");
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
