package interface_adapter.gpt;

import java.util.List;
import java.util.stream.Collectors;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Angela.DailyLog;
import entity.Angela.DailyTaskSummary;

/**
 * Builds a prompt asking the LLM for a weekly productivity analysis.
 * The LLM must return JSON with this shape:
 * {
 *   "analysis": "plain-text paragraph",
 *   "extra_notes": "missing/partial data notes or empty string"
 * }
 */
public final class GeneralAnalysisPromptBuilder {

    private GeneralAnalysisPromptBuilder() {

    }

    /**
     * Builds a prompt from 7 daily logs (last Monday to Sunday) with instructions
     * to analyze productivity and wellness trends.
     *
     * @param logs daily logs for the past week
     * @return prompt string for the LLM
     */
    public static String buildPromptFromWeeksLogs(List<DailyLog> logs) {
        final StringBuilder prompt = new StringBuilder();

        prompt.append("""
You are an analyst and coach.
Analyze the 7-day report below; do not analyze days outside these 7.
Summarize productivity patterns, trends, and correlations between completed tasks and wellness changes.
Only include one sentence about missing data if there is any.
Focus on productivity trends and wellness.

Rules for missing data:
- If any day's data (tasks, wellness, or events) is missing or partial, explicitly flag it as MISSING.
- Discuss how that gap could relate to trends seen on other days.
Clearly mark such points as assumptions (e.g., "it is possible", "may indicate").
- Do not invent exact numbers.
- If there is missing data, still analyze trends across the remaining days.
No advice verbs here.
            """);

        for (DailyLog log : logs) {
            prompt.append("=== Date: ").append(log.getDate()).append(" ===\n");

            // ------- Task summary -------
            prompt.append("Task summary:\n");
            final DailyTaskSummary summary = log.getDailyTaskSummary();
            if (summary != null) {
                final int scheduled = summary.getScheduledTasks().size();
                final int completed = summary.getCompletedTasks().size();
                final int overdue = Math.max(0, scheduled - completed);

                prompt.append("Tasks Scheduled: ").append(scheduled)
                        .append(", Completed: ").append(completed)
                        .append(", Overdue: ").append(overdue)
                        .append(", Completion rate: ").append(summary.getCompletionRate())
                        .append('\n');

                if (summary.getCategoryBreakdown() != null && !summary.getCategoryBreakdown().isEmpty()) {
                    final String categories = summary.getCategoryBreakdown().entrySet().stream()
                            .map(cat -> cat.getKey() + "=" + cat.getValue())
                            .collect(Collectors.joining(","));
                    prompt.append("Category breakdown: ").append(categories).append('\n');
                }
                else {
                    prompt.append("No task data available for this date.\n");
                }
            }
            else {
                prompt.append("No task data available for this date.\n");
            }

            // ------- Wellness logs -------
            prompt.append("Wellness Log:\n");
            final DailyWellnessLog wellnessLog = log.getDailyWellnessLog();
            if (wellnessLog != null && wellnessLog.getEntries() != null && !wellnessLog.getEntries().isEmpty()) {
                for (WellnessLogEntryInterf entry : wellnessLog.getEntries()) {
                    prompt.append("--- ")
                            .append("Stress level: ").append(entry.getStressLevel()).append(", ")
                            .append("Energy level: ").append(entry.getEnergyLevel()).append(", ")
                            .append("Fatigue level: ").append(entry.getFatigueLevel()).append(", ")
                            .append("Mood: ").append(entry.getMoodLabel().getName())
                            .append("; analyze how this mood may influence the above wellness levels.");
                    if (entry.getUserNote() != null && !entry.getUserNote().isBlank()) {
                        prompt.append(" Note: ").append(entry.getUserNote());
                    }
                    prompt.append('\n');
                }
            }
            else {
                prompt.append("No wellness data on record for this date.\n");
            }

            // ------- Event logs -------
            prompt.append("Events:\n");
            final DailyEventLog events = log.getDailyEventLog();
            if (events != null && events.getActualEvents() != null && !events.getActualEvents().isEmpty()) {
                for (EventInterf event : events.getActualEvents()) {
                    prompt.append("- ").append(event.getInfo().getName()).append('\n');
                }
            }
            else {
                prompt.append("No events data for this date.\n");
            }

            prompt.append('\n');
        }

        prompt.append("WEEK DATA END.\n\n");

        prompt.append("""
OUTPUT REQUIREMENTS:
Return STRICT JSON only, no markdown. Use this schema exactly:
{
  "analysis":    string,   // trends and correlations, no advice
  "extra_notes": string    // notes on missing/partial data ("" if none)
}
                """);

        return prompt.toString();
    }
}
