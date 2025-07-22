package interface_adapter.gpt;

import entity.DailyLog;
import entity.DailyTaskSummary;

import java.util.List;

public class PromptBuilder {
    public static String buildPromptFromDailyLog(List<DailyLog> logs) {
        // idea: extract info: completed tasks, stress lvl, moods, overdues, etc
        // build coherent natural language prompt

        StringBuilder prompt = new StringBuilder();

        prompt.append("Analyze the following for the past week's " +
                "user productivity and wellness insights and return a feedback entry:");
        prompt.append("This week's date: "); //add extractor for this week date


        for (DailyLog log: logs) { // Daily Summary Info
            DailyTaskSummary summary = log.getDailyTaskSummary();
            int scheduled = summary.getScheduledTasks().size();
            int completed = summary.getCompletedTasks().size();
            int overdue = scheduled - completed;

            prompt.append("Tasks Scheduled: ")
                    .append(scheduled)
                    .append(", Completed: ")
                    .append(completed)
                    .append(", Overdue: ")
                    .append(overdue)
                    .append("Completion rate:")
                    .append(summary.getCompletionRate())
                    .append("\n");

//            if (!summary.getCategoryBreakdown().isEmpty()) {
//                prompt.append("Completion by category: ");
//                // I don't know how to write yet TAT
//                //TODO: Implement this! -From ina to ina
//            }
//
//            //Event logs
//            prompt.append("Events: ");
//            DailyEventLog events;
//            // TODO: Require Implementation of DailyEventLog
//
//            // Wellness logs
//            prompt.append("Wellness Log:");
//            DailyWellnessLog wellnessLog = log.getDailyWellnessLog();
//            for (WellnessLogEntry entry : wellnessLog.getEntries()) {
//
//            }
        }
        // TODO: Require Implementation of WellnessLog entity from

        return String.valueOf(prompt);

    }
}
