package interface_adapter.gpt;

import entity.DailyLog;
import entity.DailyTaskSummary;

public class PromptBuilder {
    public static String buildPromptFromDailyLog(DailyLog log) {
        // idea: extract info: completed tasks, stress lvl, moods, overdues, etc
        // build coherent natural language prompt

        StringBuilder sb = new StringBuilder();

        int numCompletedTasks = log.getDailyTaskSummary().getCompletedTasks().size();
        int num

    }
}
