package entity;

import java.time.LocalDate;

public class FeedbackEntry {
    private final LocalDate date;
    private final String aiAnalysis;
    private final String recommendations;
    private final String correlationData = "";

    public FeedbackEntry(
            DailyLog log,
            String aiAnalysis,
            String recommendations) {
        this.date = log.getDate();
        this.aiAnalysis = aiAnalysis;
        this.recommendations = recommendations;
        this.correlationData = correlationData;
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
                "Analysis: " + aiAnalysis + '\'' +
                "Recommendations given:" + recommendations +'\'' +
                "Correlation:" + correlationData + '\'';
    }
}
