package entity;

import java.time.LocalDate;

public class FeedbackEntry {
    private final LocalDate date;
    private final String aiAnalysis;
    private final String correlationData;
    private final String recommendations;

    public FeedbackEntry(LocalDate date, String aiAnalysis, String correlationData, String recommendations) {
        this.date = date;
        this.aiAnalysis = aiAnalysis;
        this.correlationData = correlationData;
        this.recommendations = recommendations;
    }


    public String toString() {
        return "Feedback Entry for"
    }
}
