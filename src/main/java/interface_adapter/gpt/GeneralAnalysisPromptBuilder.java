package interface_adapter.gpt;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Angela.DailyLog;
import entity.Angela.DailyTaskSummary;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Output a JSON regarding users weekly productivity
 * {
 *   "analysis": "…plain‑text paragraph…",
 *   "extra_notes": "…anything you want flagged (e.g., missing data)…"
 * }
 */
public class GeneralAnalysisPromptBuilder {
    public static String buildPromptFromWeeksLogs(List<DailyLog> logs) {
        // idea: extract info: completed tasks, stress lvl, moods, overdues, etc
        // build coherent natural language prompt

        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an analyst and coach. Analyse the 7-day report below, DO NOT ANALYZE ANY OTHER DAYS OUTSIDE OF THE 7 DAYS" +
                        "summarize productivity patterns trends, correlations between amount of completed tasks, average wellness changes: \n " +
                        "Only give one sentence discussing missing data across the week if there are. Must focus on productivity trend and wellness")
                .append("Rules for missing data: If any day's data (tasks, wellness, or events) is missing or partial, explicitly flag it as MISSING.\n")
                .append("Discuss how that gap could relate to productivity or wellness trends seen on other days, " +
                        "but clearly mark such points as assumptions or possibilities (e.g., 'it is possible', 'may indicate'). " +
                        "Do NOT invent exact numbers.\n")
                .append("Focus on trends, correlations, and data gaps across different days. Analyze trends across remaining days if there is missing data. NO advice verbs there.\n");

        for (DailyLog log: logs) {
            prompt.append("=== Date: ").append(log.getDate()).append(" ===\n");

            // ------- Task summary ------
            prompt.append("Task summary: \n");
            DailyTaskSummary summary = log.getDailyTaskSummary();
            if (summary != null) {
                int scheduled = summary.getScheduledTasks().size();
                int completed = summary.getCompletedTasks().size();
                int overdue = scheduled - completed;

                prompt.append("Tasks Scheduled: ").append(scheduled)
                        .append(", Completed: ").append(completed)
                        .append(", Overdue: ").append(overdue)
                        .append("Completion rate:").append(summary.getCompletionRate())
                        .append("\n");

                if (!summary.getCategoryBreakdown().isEmpty() && summary.getCategoryBreakdown() != null) {
                    String categories = summary.getCategoryBreakdown().entrySet().stream()
                            .map(e -> e.getKey() + "=" + e.getValue())
                            .collect(Collectors.joining(","));
                    prompt.append("Category breakdown: ").append(categories).append('\n');
                } else {
                    prompt.append("No task data available for this date.");
                }
            }

            // ------Wellness logs ------
            prompt.append("Wellness Log: \n");
            DailyWellnessLog wellnessLog = log.getDailyWellnessLog();
            if (wellnessLog != null && wellnessLog.getEntries() != null && !wellnessLog.getEntries().isEmpty()) {
                for (WellnessLogEntry entry : wellnessLog.getEntries()) {
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
            } else {
                prompt.append("No wellness data on record for this date.");
            }

            // ------ Event logs ------
            prompt.append("Events: ");
            DailyEventLog events = log.getDailyEventLog();
            if (events != null && events.getActualEvents() != null && !events.getActualEvents().isEmpty()) {
                for (Event event : events.getActualEvents()) {
                    prompt.append("- ").append(event.getInfo().getName()).append('\n');
                }
            } else {
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
}
