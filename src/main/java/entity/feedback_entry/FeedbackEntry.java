package entity.feedback_entry;

import java.time.LocalDate;

public class FeedbackEntry implements FeedbackEntryInterf {
    private final LocalDate date;
    private final String aiAnalysis;
    private final String correlationData;
    private final String recommendations;

    public FeedbackEntry(LocalDate date,
                         String aiAnalysis,
                         String correlationData,
                         String recommendations) {
        this.date = date;
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

    /**
     * Display feedbackEntry in form of string literal.
     * @return String form of feedback entry
     */
    public String toString() {
        return "Feedback Entry on" + date + ':' + '\''
                + "Wellness analysis: " + aiAnalysis + '\''
                + "Task vs productivity correlation:" + correlationData + '\''
                + "Recommendations given:" + recommendations + '\'';
    }

    public LocalDate getDate() {
        return date;
    }
}
