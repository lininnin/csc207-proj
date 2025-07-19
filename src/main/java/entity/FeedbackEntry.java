package entity;

import java.time.LocalDate;

public class FeedbackEntry {
    private final LocalDate date;
    private final String aiAnalysis;
    private final String correlationData;
    private final String recommendations;
    // TODO: Check algorithm for generate feedback use case

    public FeedbackEntry(LocalDate date,
                         String aiAnalysis,
                         String correlationData,
                         String recommendations) {
        this.date = date;
        // TODO: Prompt for AI to analyze correlation to be implemented in PromptBuilder?
        //  check what we want
        this.aiAnalysis = aiAnalysis;
        this.correlationData = correlationData;
        this.recommendations = recommendations;

    }

    public String getAiAnalysis() {
        return aiAnalysis;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public String getCorrelationData() {
        return correlationData;
    }


    public String toString() {
        return "Feedback Entry on" + date + ':' + '\'' +
                "Wellness nalysis: " + aiAnalysis + '\'' +
                "Task vs productivity correlation:" + correlationData + '\'' +
                "Recommendations given:" + recommendations +'\'';
    }

    public Object getDate() {
        return date;
    }
}
