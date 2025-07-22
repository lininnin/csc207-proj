package interface_adapter.gpt;

public final class DefaultPromptProvider {
    private DefaultPromptProvider() {}

    public static final String DEFAULT_PROMPT =
            "Analyze the following daily report and return a feedback entry: " +
                    "{\"analysis\":\"...\",\"recommendations\":\"...\"}.\n\n" +
                    "Date: 2025-01-01\n" +
                    "Tasks Scheduled: 6, Completed: 4, Overdue: 2, Completion Rate: 0.67\n" +
                    "Completion by Category: work=2, personal=1, health=1\n\n" +
                    "Wellness Logs:\n" +
                    "- 08:30  Stress=3, Energy=6, Fatigue=2, Mood=Calm\n" +
                    "- 14:00  Stress=6, Energy=5, Fatigue=4, Mood=Stressed\n" +
                    "- 21:30  Stress=5, Energy=3, Fatigue=6, Mood=Tired\n\n" +
                    "Events:\n" +
                    "- morning walk\n- team meeting\n- late workout\n\n" +
                    "analysis: Describe patterns only (no advice verbs).\n" +
                    "recommendations: 3-5 imperative steps.\n";
}
